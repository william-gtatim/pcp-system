package tatim.william.api;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tatim.william.application.planning.ProductionPlanningService;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/production-planning")
public class ProductionPlanningController {
    @Inject
    ProductionPlanningService service;

    @GET
    public Response get(){
        return Response.ok(service.plan()).build();
    }

}
