package tatim.william.application.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import tatim.william.application.DuplicatedeCodeException;
import tatim.william.application.GenerateCodeService;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;

@ApplicationScoped
public class CreateProductUseCase {
    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;
    @Inject
    GenerateCodeService codeService;

    @Transactional
    public ProductResponse create(ProductRequest dto){


        int MAX_RETRIES = 10;

        for(int attempt = 1; attempt <= MAX_RETRIES; attempt++){
            try {
                var product = mapper.toEntity(dto);
                product.setCode("P-" + codeService.generate());
                repository.persist(product);

                return mapper.toDto(product);

            }catch (ConstraintViolationException e){
                // retry on constraint violation
            }
        }

        throw new DuplicatedeCodeException("Não foi possível criar um código para o produto. Tente salvar novamente.");

    }
}
