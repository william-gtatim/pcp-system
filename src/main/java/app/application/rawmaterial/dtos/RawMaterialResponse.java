package tatim.william.application.rawmaterial.dtos;

public record RawMaterialResponse(
        Long id,
        String code,
        String name,
        Long stockQuantity
) {
}
