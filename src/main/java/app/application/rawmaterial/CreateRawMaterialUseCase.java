package tatim.william.application.rawmaterial;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import tatim.william.application.DuplicatedeCodeException;
import tatim.william.application.GenerateCodeService;
import tatim.william.application.rawmaterial.dtos.RawMaterialRequest;
import tatim.william.domain.rawmaterial.RawMaterial;

@ApplicationScoped
public class CreateRawMaterialUseCase {

    @Inject
    RawMaterialRepository repository;

    @Inject
    GenerateCodeService generateCodeService;

    @Inject
    RawMaterialMapper mapper;


    @Transactional
    public RawMaterial create(RawMaterialRequest dto){
        int MAX_RETRIES = 10;
        for(int attempt = 1; attempt <= MAX_RETRIES; attempt ++){
            try {
                var rawmaterial = mapper.toEntity(dto);
                rawmaterial.setCode("MP-" +generateCodeService.generate());
                repository.persist(rawmaterial);
                return rawmaterial;

            }catch (ConstraintViolationException e){
                // retry on constraint violation
            }
        }

        throw new DuplicatedeCodeException("Não foi possível criar um código para a matéria-prima. Tente salvar novamente.");

    }
}
