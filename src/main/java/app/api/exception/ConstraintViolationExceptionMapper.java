package tatim.william.api.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
@Provider
public class ConstraintViolationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        List<ErrorDetail> errors = exception.getConstraintViolations()
                .stream()
                .map(v -> new ErrorDetail(
                        extractField(v.getPropertyPath().toString()),
                        v.getMessage()
                ))
                .toList();

        ErrorResponse response = new ErrorResponse(
                422,
                "Erro de validação",
                errors
        );

        return Response
                .status(422)
                .entity(response)
                .build();
    }

    private String extractField(String path) {
        int lastDot = path.lastIndexOf('.');
        return lastDot != -1 ? path.substring(lastDot + 1) : path;
    }
}