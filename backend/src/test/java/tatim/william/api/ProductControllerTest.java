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

        Long productId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                   "name": "Produto",
                                   "price": 1.34
                                }
                            """)
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
                   "price": 9.99
                }
            """)
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