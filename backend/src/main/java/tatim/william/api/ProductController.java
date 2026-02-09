package tatim.william.api;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tatim.william.application.product.CreateProductUseCase;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.ProductService;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/products")
public class ProductController {

    @Inject
    ProductService service;

    @Inject
    CreateProductUseCase createProductUseCase;

    @POST
    public Response create(ProductRequest dto){

        return Response.ok(createProductUseCase.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") Long id,
            ProductRequest dto){

        return Response.ok(service.update(dto, id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.ok().build();
    }

    @GET
    public Response list(){
        return Response.ok(service.list()).build();
    }
}
