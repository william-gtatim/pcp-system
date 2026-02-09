package tatim.william.api.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class EntityNotFoundExceptionMapper
        implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException ex) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(
                        404,
                        ex.getMessage(),
                        List.of()
                ))
                .build();
    }
}