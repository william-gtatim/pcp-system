package tatim.william.application.product.composition.dtos;

public record ProductCompositionResponse(
        Long id,
        Long productId,
        Long rawMaterialId,
        String rawMaterialName,
        Float quantityRequired
) {
}
