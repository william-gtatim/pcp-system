package tatim.william.application.planning;

import java.math.BigDecimal;

public record ProductionPlanningResponse(
        String code,
        String name,
        BigDecimal totalRevenue,
        int quantity
) {
}
