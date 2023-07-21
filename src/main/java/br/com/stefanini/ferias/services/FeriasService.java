package br.com.stefanini.ferias.services;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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

	                List<PeriodoFerias> periodosFerias = new ArrayList<>();
	                addPeriodoFerias(record, 1, periodosFerias);
	                addPeriodoFerias(record, 2, periodosFerias);
	                addPeriodoFerias(record, 3, periodosFerias);

	                for (PeriodoFerias periodoFerias : periodosFerias) {
	                    calcularDiasFerias(nome, periodoFerias.getInicio(), periodoFerias.getFim(), periodoFerias.getNumDias(), feriasPorProfissionalEMes);
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


	private static void calcularDiasFerias(String nome, LocalDate inicio, LocalDate fim, int numDias,
			Map<String, Map<String, Integer>> feriasPorProfissionalEMes) {
		if (inicio != null && fim != null) {
			LocalDate dataAtual = inicio;
			while (!dataAtual.isAfter(fim)) {
				String mes = getMesFormatado(dataAtual);

				// Verificar se a data está dentro do período de férias considerando o mês de
				// início no dia 21
				if (dataAtual.getDayOfMonth() >= 21) {
					// Armazenar os dias de férias para cada mês e cada profissional
					feriasPorProfissionalEMes.computeIfAbsent(nome, k -> new HashMap<>());
					feriasPorProfissionalEMes.get(nome).merge(mes, numDias, Integer::sum);
				}

				dataAtual = dataAtual.plusDays(1);
			}
		}
	}

	private static String getMesFormatado(LocalDate date) {
		LocalDate inicioMes = date.withDayOfMonth(21);
		if (date.isBefore(inicioMes)) {
			inicioMes = inicioMes.minusMonths(1);
		}
		int monthValue = inicioMes.getMonthValue();
		int year = inicioMes.getYear();
		return String.format("%02d/%d", monthValue, year);
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