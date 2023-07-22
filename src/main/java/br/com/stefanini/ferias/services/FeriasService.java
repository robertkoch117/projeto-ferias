package br.com.stefanini.ferias.services;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeriasService {

	public String montaRelatorio(String csvFile) {

		try (FileReader fileReader = new FileReader(csvFile);
	             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';'))) {

	            // Mapeamento para armazenar o total de dias de férias por mês para cada profissional
	            Map<String, Map<String, Integer>> feriasPorProfissionalEMes = new HashMap<>();

	            for (CSVRecord record : csvParser) {
	                String nome = record.get("Colaborador");
	                String situacao = record.get("Situação (Férias)");

	                if (situacao.equals("Parcial") || situacao.equals("Pendente")) {
	                    List<PeriodoFerias> periodosFerias = new ArrayList<>();
	                    
	                    addPeriodoFerias(record, 1, periodosFerias);
	                    addPeriodoFerias(record, 2, periodosFerias);
	                    addPeriodoFerias(record, 3, periodosFerias);

	                    for (PeriodoFerias periodoFerias : periodosFerias) {
	                        calcularDiasFeriasPorMes(nome, periodoFerias, feriasPorProfissionalEMes);
	                    }
	                }
	            }

	            // Construir a resposta no formato desejado
	            StringBuilder responseBuilder = new StringBuilder();
	            Map<String, ResultadoMes> resultadosPorMes = new HashMap<>();

	            for (Map.Entry<String, Map<String, Integer>> entry : feriasPorProfissionalEMes.entrySet()) {
	                String nomeProfissional = entry.getKey();
	                Map<String, Integer> feriasPorMes = entry.getValue();

	                for (Map.Entry<String, Integer> feriasMes : feriasPorMes.entrySet()) {
	                    String mes = feriasMes.getKey();
	                    int diasFerias = feriasMes.getValue();

	                    resultadosPorMes.computeIfAbsent(mes, k -> new ResultadoMes(mes));

	                    ResultadoMes resultadoMes = resultadosPorMes.get(mes);
	                    resultadoMes.addProfissional(nomeProfissional, diasFerias);
	                }
	            }

	            for (ResultadoMes resultadoMes : resultadosPorMes.values()) {
	                responseBuilder.append(resultadoMes.getDescricao()).append(": ").append(resultadoMes.getTotalDias()).append(" dias\n");
	                for (Map.Entry<String, Integer> profissionalEntry : resultadoMes.getProfissionais().entrySet()) {
	                    String profissional = profissionalEntry.getKey();
	                    int diasFerias = profissionalEntry.getValue();
	                    responseBuilder.append("    ").append(profissional).append(" = ").append(diasFerias).append(" dias\n");
	                }
	            }

	            return responseBuilder.toString();

	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Erro ao processar o arquivo CSV.";
	        }
	    }

	
	// Método auxiliar para adicionar os períodos de férias
    private void addPeriodoFerias(CSVRecord record, int numeroPeriodo, List<PeriodoFerias> periodosFerias) {
        LocalDate inicio = parseDate(record.get("Data Saída Parc. " + numeroPeriodo));
        LocalDate retorno = parseDate(record.get("Data Retorno Parc. " + numeroPeriodo));
        int numDias = parsePositiveInt(record.get("Nº Dias Parc. " + numeroPeriodo));

        if (inicio != null && retorno != null && numDias > 0) {
            periodosFerias.add(new PeriodoFerias(inicio, retorno, numDias));
        }
    }


    // Classe auxiliar para representar os períodos de férias
    private static class PeriodoFerias {
        private final LocalDate inicio;
        private final LocalDate fim;
        private final int numDias;

        public PeriodoFerias(LocalDate inicio, LocalDate fim, int numDias) {
            this.inicio = inicio;
            this.fim = fim;
            this.numDias = numDias;
        }

        public LocalDate getInicio() {
            return inicio;
        }

        public LocalDate getFim() {
            return fim;
        }

        public int getNumDias() {
            return numDias;
        }
    }
    
    private void calcularDiasFeriasPorMes(String nome, PeriodoFerias periodoFerias, Map<String, Map<String, Integer>> feriasPorProfissionalEMes) {
        LocalDate inicio = periodoFerias.getInicio();
        LocalDate fim = periodoFerias.getFim();
        int numDias = periodoFerias.getNumDias();

        int ano = inicio.getYear();
        int mesInicio = inicio.getMonthValue();
        int mesFim = fim.getMonthValue();
        int diasUtilizados = 0;

        for (int mes = mesInicio; mes <= mesFim; mes++) {
            YearMonth anoMes = YearMonth.of(ano, mes);
            int diasMes = anoMes.lengthOfMonth();
            int diaFinalDoMes = YearMonth.of(ano, mes).plusMonths(1).atDay(20).getDayOfMonth();

            if (mes == mesInicio) {
                // Ajusta o início para o primeiro dia do mês atual
                inicio = inicio.withDayOfMonth(1);
            } else {
                // Ajusta o início para o dia 21 do mês atual
                inicio = YearMonth.of(ano, mes).atDay(21);
            }

            if (mes == mesFim) {
                // Verifica se o dia 20 é maior que o último dia do mês atual
                if (diaFinalDoMes > diasMes) {
                    // Ajusta o fim para o último dia do mês atual
                    fim = YearMonth.of(ano, mes).atDay(diasMes);
                } else {
                    // Ajusta o fim para o dia 20 do mês atual
                    fim = YearMonth.of(ano, mes).atDay(diaFinalDoMes);
                }
            } else {
                // Ajusta o fim para o dia 20 do mês atual
                fim = YearMonth.of(ano, mes).atDay(diaFinalDoMes);
            }

            int diasNoPeriodo = (int) inicio.datesUntil(fim.plusDays(1)).count();
            int diasUtilizadosNoMes = Math.min(numDias, diasNoPeriodo);
            diasUtilizados += diasUtilizadosNoMes;
            numDias -= diasUtilizadosNoMes;
        }

        if (diasUtilizados > 0) {
            String mesAno = mesFim + "/" + ano;
            feriasPorProfissionalEMes.computeIfAbsent(nome, k -> new HashMap<>());
            feriasPorProfissionalEMes.get(nome).merge(mesAno, diasUtilizados, Integer::sum);
        }
    }

	private static LocalDate parseDate(String dateStr) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return "-".equals(dateStr) ? null : LocalDate.parse(dateStr, formatter);
		} catch (Exception e) {
			return null;
		}
	}

	private static int parsePositiveInt(String intStr) {
		try {
			return "-".equals(intStr) ? 0 : Math.max(Integer.parseInt(intStr), 0);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static class ResultadoMes {
		private final String descricao;
		private final Map<String, Integer> profissionais;
		private int totalDias;

		public ResultadoMes(String descricao) {
			this.descricao = descricao;
			this.profissionais = new HashMap<>();
			this.totalDias = 0;
		}

		public String getDescricao() {
			return descricao;
		}

		public Map<String, Integer> getProfissionais() {
			return profissionais;
		}

		public int getTotalDias() {
			return totalDias;
		}

		public void addProfissional(String nomeProfissional, int diasFerias) {
			profissionais.put(nomeProfissional, diasFerias);
			totalDias += diasFerias;
		}
	}
}