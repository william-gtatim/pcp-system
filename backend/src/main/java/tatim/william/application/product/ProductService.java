package tatim.william.application.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tatim.william.application.product.dtos.ProductRequest;
import tatim.william.application.product.dtos.ProductResponse;

import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;


    @Transactional
    public ProductResponse update(ProductRequest dto, Long productId){
        var product = repository.findById(productId);
        if(product == null){
            throw new EntityNotFoundException("O produto não existe");
        }
        mapper.updateEntity(dto, product);
        repository.persist(product);
        return mapper.toDto(product);

    }

    @Transactional
    public  void delete(Long productId){
        var product = repository.findById(productId);
        if(product == null){
            throw new EntityNotFoundException("O produto não existe");
        }

        repository.delete(product);
    }

    public List<ProductResponse> list(){
        var products = repository.findAll();
        return products
                .stream()
                .map(mapper::toDto)
                .toList();
    }



}
