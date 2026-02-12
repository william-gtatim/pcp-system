package tatim.william.application.planning;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tatim.william.application.product.ProductService;
import tatim.william.domain.product.Product;
import tatim.william.domain.product.ProductComposition;
import tatim.william.domain.rawmaterial.RawMaterial;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductionPlanningService {

    @Inject
    ProductService productService;

    public ProductionPlanningResult plan() {
        var products = productService.getAllProductsWithCompositions();

        var remainingStock = buildRemainingStock(products);

        var ordered = products.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .toList();

        List<ProductionPlanningResponse> items = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (var product : ordered) {
            int producible = maxProducibleWithRemainingStock(product, remainingStock);

            if (producible <= 0) {
                continue;
            }

            consumeStock(product, producible, remainingStock);

            var revenue = calculateTotalRevenue(product.getPrice(), producible);
            totalRevenue = totalRevenue.add(revenue);

            items.add(new ProductionPlanningResponse(
                    product.getCode(),
                    product.getName(),
                    revenue,
                    producible
            ));
        }

        return new ProductionPlanningResult(items, totalRevenue);
    }

    private Map<Long, Long> buildRemainingStock(List<Product> products) {
        Map<Long, Long> remaining = new HashMap<>();
        for (var product : products) {
            for (var pc : product.getComposition()) {
                var rm = pc.getRawMaterial();
                if (rm != null && rm.getId() != null) {
                    remaining.putIfAbsent(rm.getId(), rm.getStockQuantity());
                }
            }
        }
        return remaining;
    }

    private int maxProducibleWithRemainingStock(Product product, Map<Long, Long> remainingStock) {
        int max = Integer.MAX_VALUE;

        for (ProductComposition pc : product.getComposition()) {
            RawMaterial rm = pc.getRawMaterial();
            if (rm == null || rm.getId() == null) {
                return 0;
            }

            long available = remainingStock.getOrDefault(rm.getId(), 0L);
            float required = pc.getQuantityRequired();

            if (required <= 0f) {
                return 0;
            }

            int possible = (int) Math.floor(available / required);
            max = Math.min(max, possible);

            if (max == 0) {
                return 0;
            }
        }

        return max == Integer.MAX_VALUE ? 0 : max;
    }

    private void consumeStock(Product product, int quantity, Map<Long, Long> remainingStock) {
        for (ProductComposition pc : product.getComposition()) {
            var rm = pc.getRawMaterial();
            long available = remainingStock.getOrDefault(rm.getId(), 0L);

            float required = pc.getQuantityRequired();
            double consumed = required * quantity;

            long updated = (long) Math.floor(available - consumed);
            remainingStock.put(rm.getId(), Math.max(updated, 0L));
        }
    }

    private BigDecimal calculateTotalRevenue(BigDecimal price, int quantity) {
        return price
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }
}