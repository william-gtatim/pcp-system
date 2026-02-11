package tatim.william.application.product.composition.dtos;

public record ProductCompositionResponse(
        Long rawMaterialId,
        String rawMaterialName,
        Float quantityRequired
) {
}
