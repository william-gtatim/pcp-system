package tatim.william.application.product;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ProductRepositoryTest {

    @Inject
    ProductRepository repository;

    @Inject
    jakarta.persistence.EntityManager entityManager;

    private RawMaterial createRawMaterial(String code, String name, Long stock) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(code);
        rawMaterial.setName(name);
        rawMaterial.setStockQuantity(stock);
        entityManager.persist(rawMaterial);
        return rawMaterial;
    }

    private Product createProduct(String code, String name) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setPrice(new BigDecimal("10"));
        product.setComposition(new ArrayList<>());
        entityManager.persist(product);
        return product;
    }

    @Test
    @Transactional
    void shouldReturnProductsWithCompositions() {

        RawMaterial rawMaterial = createRawMaterial("RM-1", "Farinha", 100L);

        Product product = createProduct("Pão", "P-0");

        ProductComposition composition = new ProductComposition();
        composition.setProduct(product);
        composition.setRawMaterial(rawMaterial);
        composition.setQuantityRequired(2f);

        product.getComposition().add(composition);

        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();

        List<Product> result = repository.findAllProductsWhitCompositions();

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getComposition().size());
        assertNotNull(result.getFirst().getComposition().getFirst().getRawMaterial());
    }

    @Test
    @Transactional
    void shouldReturnTrueWhenRawMaterialIsUsed() {

        RawMaterial rawMaterial = createRawMaterial("RM-2", "Açúcar", 50L);

        Product product = createProduct("Bolo", "P2");

        ProductComposition composition = new ProductComposition();
        composition.setProduct(product);
        composition.setRawMaterial(rawMaterial);
        composition.setQuantityRequired(3f);

        product.getComposition().add(composition);

        entityManager.persist(product);

        entityManager.flush();
        entityManager.clear();

        boolean result = repository.isRawMaterialUsed(rawMaterial.getId());

        assertTrue(result);
    }

    @Test
    @Transactional
    void shouldReturnFalseWhenRawMaterialIsNotUsed() {

        RawMaterial rawMaterial = createRawMaterial("RM-3", "Leite", 30L);

        entityManager.flush();
        entityManager.clear();

        boolean result = repository.isRawMaterialUsed(rawMaterial.getId());

        assertFalse(result);
    }
}