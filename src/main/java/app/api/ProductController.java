package tatim.william.api;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tatim.william.application.product.CreateProductUseCase;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.ProductService;

import java.net.URI;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/products")
public class ProductController {

    @Inject
    ProductService service;

    @Inject
    CreateProductUseCase createProductUseCase;

    @POST
    public Response create(@Valid ProductRequest dto){
        var response = createProductUseCase.create(dto);
        URI location = URI.create("/products/" + response.id());
        return Response
                .created(location)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id){
        return Response.ok(service.get(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") Long id,
            @Valid ProductRequest dto){

        return Response.ok(service.update(dto, id)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    public Response list(){
        return Response.ok(service.list()).build();
    }
}
