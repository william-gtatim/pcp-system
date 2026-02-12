package tatim.william.application.planning;

import java.math.BigDecimal;
import java.util.List;

public record ProductionPlanningResult(
        List<ProductionPlanningResponse> items,
        BigDecimal totalRevenue
) {
}