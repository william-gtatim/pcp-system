package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;



@QuarkusTest
class ProductControllerTest {

    @Test
    void shouldCreateUpdateListAndDeleteProduct() {

        Long rawMaterialId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                    "name": "Nome",
                                    "stockQuantity": 19
                                }
                                """)
                        .when()
                        .post("raw-materials")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("code", startsWith("MP-"))
                        .body("name", equalTo("Nome"))
                        .body("stockQuantity", equalTo(19))
                        .extract()
                        .jsonPath()
                        .getLong("id");

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
                                       "quantityRequired": 2.0
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
                        .body("price", equalTo(1.34f))
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
                               "quantityRequired": 3.0
                           }
                       ]
                    }
                """.formatted(rawMaterialId))
                .when()
                .put("/products/{id}", productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId.intValue()))
                .body("name", equalTo("Produto Atualizado"))
                .body("price", equalTo(9.99f));

        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("id", hasItem(productId.intValue()))
                .body("name", hasItem("Produto Atualizado"))
                .body("price", hasItem(9.99f));

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
}