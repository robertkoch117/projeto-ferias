package br.com.stefanini.ferias.resources;

import java.util.List;

import br.com.stefanini.ferias.dto.FeriasMesDTO;
import br.com.stefanini.ferias.services.FeriasService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class FeriasResource {
	
	@Inject
	FeriasService service;
	
	@GET
	@Path("v1/ferias")
	@Produces(MediaType.APPLICATION_JSON)
	public Response relatorioFerias(@QueryParam("csv") String csvFile) {
		List<FeriasMesDTO> montaRelatorio = service.montaRelatorio(csvFile);
		return Response.ok(montaRelatorio).build();
	}
}
