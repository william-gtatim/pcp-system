package tatim.william.application.rawmaterial;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tatim.william.application.rawmaterial.dtos.RawMaterialRequest;
import tatim.william.application.rawmaterial.dtos.RawMaterialResponse;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.util.List;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    @Inject
    RawMaterialMapper mapper;

    public RawMaterialResponse get(Long id){
        var entity = getByIdOrThrow(id);
        return mapper.toDto(entity);
    }

    @Transactional
    public RawMaterialResponse update(RawMaterialRequest dto, Long id){
        var entity = getByIdOrThrow(id);
        mapper.updateEntity(dto, entity);
        repository.persist(entity);
        return mapper.toDto(entity);
    }

    public List<RawMaterialResponse> list(){
        var list = repository.findAll();
        return list.stream().map(mapper::toDto).toList();
    }

    @Transactional
    public void delete(Long id){
        var entity = getByIdOrThrow(id);
        repository.delete(entity);
    }


    private RawMaterial getByIdOrThrow(Long id){
        return repository.findByIdOptional(id).orElseThrow(
                () -> new EntityNotFoundException("Essa matéria-prima não existe")
        );
    }

}
