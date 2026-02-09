package tatim.william.application.product.composition.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuantityRequiredRequest(
        @NotNull(message = "A quantidade de matéria-prima é obrigatória")
        @Positive(message = "A quantidade de matéria-prima deve ser um valor positivo")
        float quantityRequired
) {
}
