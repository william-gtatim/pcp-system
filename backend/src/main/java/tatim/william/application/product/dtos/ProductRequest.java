package tatim.william.application.product.dtos;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        BigDecimal price
){
}
