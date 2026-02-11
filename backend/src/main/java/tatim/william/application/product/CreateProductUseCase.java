package tatim.william.application.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import tatim.william.application.DuplicatedeCodeException;
import tatim.william.application.GenerateCodeService;
import tatim.william.application.product.composition.ProductCompositionRepository;
import tatim.william.application.product.composition.ProductCompositionService;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;
import tatim.william.application.rawmaterial.RawMaterialService;
import tatim.william.domain.product.ProductComposition;

@ApplicationScoped
public class CreateProductUseCase {
    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;
    @Inject
    GenerateCodeService codeService;

    @Inject
    RawMaterialService rawMaterialService;

    @Transactional
    public ProductResponse create(ProductRequest dto){


        int MAX_RETRIES = 10;

        for(int attempt = 1; attempt <= MAX_RETRIES; attempt++){
            try {
                var product = mapper.toEntity(dto);
                product.setCode("P-" + codeService.generate());

                for (var item : dto.composition()) {
                    var rawMaterial = rawMaterialService.getByIdOrThrow(item.rawMaterialId());

                    var composition = new ProductComposition();
                    composition.setProduct(product);
                    composition.setRawMaterial(rawMaterial);
                    composition.setQuantityRequired(item.quantityRequired());

                    product.getComposition().add(composition);
                }

                repository.persist(product);

                return mapper.toDto(product);


            }catch (ConstraintViolationException e){
                // retry on constraint violation
            }
        }

        throw new DuplicatedeCodeException("Não foi possível criar um código para o produto. Tente salvar novamente.");

    }
}
