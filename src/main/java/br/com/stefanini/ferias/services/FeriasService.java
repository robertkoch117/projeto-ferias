package br.com.stefanini.ferias.services;

import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
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

import br.com.stefanini.ferias.dto.FeriasMesDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeriasService {

	public List<FeriasMesDTO> montaRelatorio(String csvFile) {
	    List<FeriasMesDTO> resultadoMeses = new ArrayList<>();
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

	        for (Map.Entry<String, Map<String, Integer>> entry : feriasPorProfissionalEMes.entrySet()) {
	            String mesAno = entry.getKey();
	            Map<String, Integer> feriasPorProfissional = entry.getValue();

	            FeriasMesDTO feriasMesDTO = new FeriasMesDTO();
	            feriasMesDTO.setMesAno(mesAno);
	            feriasMesDTO.setProfissionais(feriasPorProfissional);
	            
	            int totalDias = feriasPorProfissional.values().stream().mapToInt(Integer::intValue).sum();
	            feriasMesDTO.setTotalDias(totalDias);
	            
	            resultadoMeses.add(feriasMesDTO);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Erro ao processar o arquivo CSV.");
	    }

	    return resultadoMeses;
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
    	
    	LocalDate inicioFerias = periodoFerias.getInicio();
        LocalDate fimFerias = periodoFerias.getFim();
       
        int ano = inicioFerias.getYear();
        int mesInicio = inicioFerias.getMonthValue();
        int mesFim = fimFerias.getMonthValue();

        

        for (int mes = mesInicio; mes <= mesFim; mes++) {
            
        	int diasNoPeriodo = 0;
            LocalDate dataAux = inicioFerias;

            LocalDate dataFinalMes = LocalDate.now().withMonth(mes).withYear(ano).withDayOfMonth(21);
            
            if(dataAux.isAfter(dataFinalMes)) {
            	mes++;
            }
            
            while (!dataAux.isAfter(fimFerias)) {
            	
            	if(dataAux.isEqual(dataFinalMes) || dataAux.isAfter(dataFinalMes)) {
                	break;
                }
            	
                if (isDiaUtil(dataAux)) {
                    diasNoPeriodo++;
                }
                dataAux = dataAux.plusDays(1);
            }

            if(diasNoPeriodo > 0) {
            	 String mesAno = mes + "/" + ano;
                 feriasPorProfissionalEMes.computeIfAbsent(mesAno, k -> new HashMap<>());
                 feriasPorProfissionalEMes.get(mesAno).merge(nome, diasNoPeriodo, Integer::sum);
            }
        }
    }
    
    private boolean isDiaUtil(LocalDate data) {
        DayOfWeek dayOfWeek = data.getDayOfWeek();
        return !dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY);
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
}