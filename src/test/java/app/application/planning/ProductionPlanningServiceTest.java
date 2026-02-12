package tatim.william.application.planning;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ProductionPlanningServiceTest {

    @Inject
    ProductionPlanningService planningService;

    @Test
    void shouldPlanProduction() {

        var result = planningService.plan();
        var items = result.items();

        assertEquals(1, items.size());

        var first = items.getFirst();

        assertEquals("PIZZA", first.code());
        assertEquals(4, first.quantity());
        assertEquals(new BigDecimal("400.00"), first.totalRevenue());

        assertEquals(new BigDecimal("400.00"), result.totalRevenue());
    }
}