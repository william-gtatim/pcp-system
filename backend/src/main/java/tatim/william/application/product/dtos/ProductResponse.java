package tatim.william.application.product.dtos;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String code,
        String name,
        BigDecimal price
) {
}
