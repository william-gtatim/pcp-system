package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductionPlanningControllerTest {

    @Test
    void shouldReturnProductionPlanningResult() {

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/production-planning")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("items", notNullValue())
                .body("items", instanceOf(java.util.List.class))
                .body("totalRevenue", notNullValue());
    }
}