package tatim.william.application.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tatim.william.application.product.composition.ProductCompositionMapper;
import tatim.william.application.product.composition.dtos.ProductCompositionResponse;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;
import tatim.william.application.rawmaterial.RawMaterialService;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;

    @Inject
    RawMaterialService rawMaterialService;


    @Transactional
    public ProductResponse update(ProductRequest dto, Long productId) {

        var product = getByIdOrThrow(productId);

        mapper.updateEntity(dto, product);

        product.getComposition().clear();

        for (var item : dto.composition()) {
            var rawMaterial = rawMaterialService.getByIdOrThrow(item.rawMaterialId());

            var composition = new ProductComposition();
            composition.setProduct(product);
            composition.setRawMaterial(rawMaterial);
            composition.setQuantityRequired(item.quantityRequired());

            product.getComposition().add(composition);
        }

        return mapper.toDto(product);
    }
    @Transactional
    public  void delete(Long productId){
        var product = getByIdOrThrow(productId);
        repository.delete(product);
    }

    public List<Product> getAllProductsWithCompositions(){
        return repository.findAllProductsWhitCompositions();
    }

    public ProductResponse get(Long id){
        return mapper.toDto(getByIdOrThrow(id));
    }


    public List<ProductResponse> list(){
        var products = repository.findAllProductsWhitCompositions();
        return products
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public boolean existsByRawMaterial(Long rawMaterialId){
        return repository.isRawMaterialUsed(rawMaterialId);
    }

    public Product getByIdOrThrow(Long id){
        return repository.findByIdOptional(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("O produto não existe")
                );
    }



}
