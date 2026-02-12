package tatim.william.application.rawmaterial;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tatim.william.application.rawmaterial.dtos.RawMaterialRequest;
import tatim.william.application.rawmaterial.dtos.RawMaterialResponse;
import tatim.william.domain.rawmaterial.RawMaterial;

@Mapper(componentModel = "cdi")
interface RawMaterialMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    RawMaterial toEntity(RawMaterialRequest dto);

    RawMaterialResponse toDto(RawMaterial entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    void updateEntity(RawMaterialRequest dto, @MappingTarget RawMaterial entity);
}
