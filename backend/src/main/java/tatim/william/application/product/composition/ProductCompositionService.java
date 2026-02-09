package tatim.william.application.product.composition;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tatim.william.application.product.composition.dtos.ProductCompositionResponse;
import tatim.william.application.product.ProductService;
import tatim.william.application.product.composition.dtos.ProductCompositionRequest;
import tatim.william.application.product.composition.dtos.QuantityRequiredRequest;
import tatim.william.application.rawmaterial.RawMaterialService;
import tatim.william.domain.product.ProductComposition;

@ApplicationScoped
public class ProductCompositionService {
    @Inject
    ProductCompositionRepository repository;
    @Inject
    ProductService productService;
    @Inject
    RawMaterialService rawMaterialService;
    @Inject
    ProductCompositionMapper mapper;


    @Transactional
    public ProductCompositionResponse create(ProductCompositionRequest dto, Long productId){
        var product = productService.getByIdOrThrow(productId);
        var rawMaterial = rawMaterialService.getByIdOrThrow(dto.rawMaterialId());

        var composition = new ProductComposition();
        composition.setProduct(product);
        composition.setRawMaterial(rawMaterial);
        composition.setQuantityRequired(dto.quantityRequired());

        repository.persist(composition);

        return mapper.toDto(composition);
    }

    @Transactional
    public ProductCompositionResponse updateQuantityRequired(QuantityRequiredRequest dto, Long compositionId){
        var composition = getByIdOrThrow(compositionId);
        composition.setQuantityRequired(dto.quantityRequired());
        repository.persist(composition);
        return mapper.toDto(composition);
    }


    private ProductComposition getByIdOrThrow(Long id){
        return repository.findByIdOptional(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Composição de produto não encontrada"));
    }



}
