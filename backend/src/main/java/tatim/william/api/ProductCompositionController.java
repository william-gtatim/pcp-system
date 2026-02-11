package tatim.william.api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tatim.william.application.product.composition.dtos.ProductCompositionRequest;
import tatim.william.application.product.composition.ProductCompositionService;
import tatim.william.application.product.composition.dtos.QuantityRequiredRequest;


import java.net.URI;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/products/{productId}/compositions")
public class ProductCompositionController {
    @Inject
    ProductCompositionService service;

    @PATCH
    @Path("{id}")
    public Response updateQuantityRequired(
            @PathParam("id") Long id,
            @Valid QuantityRequiredRequest dto
    ){
        var response = service.updateQuantityRequired(dto, id);

        return Response.ok(response).build();
    }

    @GET
    public Response listProductComposition(@PathParam("productId") Long productId){
        return Response.ok(service.listProductCompositions(productId)).build();
    }

}
