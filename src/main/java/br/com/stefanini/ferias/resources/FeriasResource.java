package br.com.stefanini.ferias.resources;

import br.com.stefanini.ferias.services.FeriasService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("")
public class FeriasResource {
	
	@Inject
	FeriasService service;
	
	@GET
	@Path("v1/ferias")
	public Response relatorioFerias(@QueryParam("csv") String csvFile) {
		String montaRelatorio = service.montaRelatorio(csvFile);
		return Response.ok(montaRelatorio).build();
	}
}
