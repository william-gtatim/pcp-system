package tatim.william.application.product.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tatim.william.application.product.composition.dtos.ProductCompositionRequest;


import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Positive(message = "O preço não pode ser negativo")
        @NotNull(message = "O preço é obrigatório")
        BigDecimal price,

        @NotEmpty(message = "O produto deve possuir pelo menos uma matéria-prima")
        List<ProductCompositionRequest> composition

){
}
