package tatim.william.application.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;
import tatim.william.domain.product.Product;



@Mapper(componentModel = "cdi")
public interface ProductMapper {
    Product toEntity(ProductRequest dto);
    ProductResponse toDto(Product entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    void updateEntity(ProductRequest dto, @MappingTarget Product product);
}
