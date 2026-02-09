package tatim.william.api.exception;

public record ErrorDetail(
        String field,
        String error
) {
}