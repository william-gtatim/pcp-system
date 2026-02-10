package tatim.william.api.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import tatim.william.domain.DomainException;

import java.util.List;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException ex){
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                        400,
                        ex.getMessage(),
                        List.of()
                ))
                .build();
    }
}
