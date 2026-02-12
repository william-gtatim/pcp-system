package tatim.william.application.product.composition;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tatim.william.application.product.composition.dtos.ProductCompositionResponse;
import tatim.william.domain.product.ProductComposition;

@Mapper(componentModel = "cdi")
public interface ProductCompositionMapper {
    @Mapping(target = "rawMaterialId", source = "rawMaterial.id")
    @Mapping(target = "rawMaterialName", source = "rawMaterial.name")
    ProductCompositionResponse toDto(ProductComposition entity);

}
