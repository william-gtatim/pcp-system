package tatim.william.application.product.dtos;

import tatim.william.application.product.composition.dtos.ProductCompositionResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String code,
        String name,
        BigDecimal price,
        List<ProductCompositionResponse> composition
) {
}
