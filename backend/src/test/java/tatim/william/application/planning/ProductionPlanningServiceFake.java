package tatim.william.application.planning;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import tatim.william.application.product.ProductService;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.math.BigDecimal;
import java.util.List;

@Alternative
@Priority(1)
@ApplicationScoped
@IfBuildProfile("test")
public class ProductionPlanningServiceFake extends ProductService {

    @Override
    public List<Product> getAllProductsWithCompositions() {
        var farinha = rawMaterial(1L,"RM-1", "Farinha", 100L);
        var fermento = rawMaterial(2L,"RM-2", "Fermento", 5L);
        var acucar = rawMaterial(3L,"RM-3", "Açúcar", 10L);
        var sal = rawMaterial(4L,"RM-4", "Sal", 2L);

        var massaPizza = product(
                1L,
                "PIZZA",
                "Massa de Pizza",
                new BigDecimal("100.00"), // 4 * 100 = 400
                List.of(
                        composition(farinha, 10f), // produz 10
                        composition(fermento, 0.2f), // produz 25
                        composition(sal, 0.5f) // produz 4
                )
        );

        var pao = product(
                2L,
                "PAO",
                "Pão",
                new BigDecimal("50.00"), // 10 * 50 = 500
                List.of(
                        composition(farinha, 8f), // produz 12
                        composition(fermento, 0.5f), // produz 10
                        composition(acucar, 1f), // produz 10
                        composition(sal, 0.1f) // produz 20
                )
        );

        return List.of(massaPizza, pao);
    }

    private RawMaterial rawMaterial(Long id, String code, String name, Long stock) {
        var rm = new RawMaterial();
        rm.setId(id);
        rm.setCode(code);
        rm.setName(name);
        rm.setStockQuantity(stock);
        return rm;
    }

    private Product product(
            Long id,
            String code,
            String name,
            BigDecimal price,
            List<ProductComposition> compositions
    ) {
        var product = new Product();
        product.setId(id);
        product.setCode(code);
        product.setName(name);
        product.setPrice(price);

        for (var pc : compositions) {
            pc.setProduct(product);
        }
        product.setComposition(compositions);

        return product;
    }

    private ProductComposition composition(RawMaterial rm, float required) {
        var pc = new ProductComposition();
        pc.setRawMaterial(rm);
        pc.setQuantityRequired(required);
        return pc;
    }
}