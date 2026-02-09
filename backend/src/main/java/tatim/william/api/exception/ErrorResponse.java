package tatim.william.api.exception;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<ErrorDetail> errors
){}
