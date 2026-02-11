package tatim.william.application.planning;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tatim.william.application.product.ProductService;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductionPlanningService {
    @Inject
    ProductService productService;

    public List<ProductionPlanningResponse> plan(){
        var products = productService.getAllProductsWithCompositions();

        Map<Product, Integer> production = new HashMap<>();

        for(Product product: products) {
            for(ProductComposition pc : product.getComposition()){
                var required = pc.getQuantityRequired();
                var availability = pc.getRawMaterial().getStockQuantity();
                int pd = (int) Math.max(0, availability / required);
                var current = production.getOrDefault(product, Integer.MAX_VALUE);
                production.put(product, Math.min(pd, current));
            }
        }

        return products.stream().map(
                (item) -> new ProductionPlanningResponse(
                        item.getCode(),
                        item.getName(),
                        calculateTotalRevenue(item.getPrice(), production.get(item)),
                        production.get(item)
                )
            ).sorted(Comparator.comparing(
                    ProductionPlanningResponse::totalRevenue
                    ).reversed())
                .toList();

    }


    private BigDecimal calculateTotalRevenue(BigDecimal price, int quantity){
        return price
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
