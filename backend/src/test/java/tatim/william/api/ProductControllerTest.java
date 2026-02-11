package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductControllerTest {

    private Long createRawMaterial() {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                           "name": "Farinha",
                           "stockQuantity": 100
                        }
                      """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    @Test
    void shouldCreateUpdateListAndDeleteProduct() {

        Long rawMaterialId = createRawMaterial();

        Long productId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                   "name": "Produto",
                                   "price": 1.34,
                                   "composition": [
                                       {
                                           "rawMaterialId": %d,
                                           "quantityRequired": 2
                                       }
                                   ]
                                }
                              """.formatted(rawMaterialId))
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("code", startsWith("P-"))
                        .body("name", equalTo("Produto"))
                        .body("composition", hasSize(1))
                        .extract()
                        .jsonPath()
                        .getLong("id");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                           "name": "Produto Atualizado",
                           "price": 9.99,
                           "composition": [
                               {
                                   "rawMaterialId": %d,
                                   "quantityRequired": 5
                               }
                           ]
                        }
                      """.formatted(rawMaterialId))
                .when()
                .put("/products/{id}", productId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Produto Atualizado"))
                .body("price", equalTo(9.99f));

        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("id", hasItem(productId.intValue()));

        given()
                .when()
                .delete("/products/{id}", productId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/products/{id}", productId)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnErrorWhenCompositionIsEmpty() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                           "name": "Produto",
                           "price": 10,
                           "composition": []
                        }
                      """)
                .when()
                .post("/products")
                .then()
                .statusCode(422);
    }

    @Test
    void shouldReturn400WhenRawMaterialDoesNotExist() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                           "name": "Produto",
                           "price": 10,
                           "composition": [
                               {
                                   "rawMaterialId": 999999,
                                   "quantityRequired": 2
                               }
                           ]
                        }
                      """)
                .when()
                .post("/products")
                .then()
                .statusCode(anyOf(is(400), is(404)));
    }

    @Test
    void shouldReturn400WhenQuantityIsNegative() {

        Long rawMaterialId = createRawMaterial();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                           "name": "Produto",
                           "price": 10,
                           "composition": [
                               {
                                   "rawMaterialId": %d,
                                   "quantityRequired": -1
                               }
                           ]
                        }
                      """.formatted(rawMaterialId))
                .when()
                .post("/products")
                .then()
                .statusCode(400)
                .body(containsString("maior que zero"));
    }
}