package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@QuarkusTest
class ProductCompositionControllerTest {
    Long productId;
    Long rawMaterialId;
    Long compositionId;

    @BeforeEach
    void setup() {

        productId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                        "name": "Produto Teste",
                        "price": 10.0
                    }
                """)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");

        rawMaterialId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                        "name": "Matéria-Prima Teste",
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

        compositionId =
                        given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                    "rawMaterialId": %d,
                                    "quantityRequired": 2.5
                                }
                            """.formatted(rawMaterialId))
                        .when()
                        .post("/products/{productId}/composition", productId)
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()
                        .getLong("id");
    }

    @Nested
    class CreateComposition{
        @Test
        void shouldCreateProductComposition() {

            given()
                    .contentType(ContentType.JSON)
                    .body("""
                {
                    "rawMaterialId": %d,
                    "quantityRequired": 2.5
                }
            """.formatted(rawMaterialId))
                    .when()
                    .post("/products/{productId}/composition", productId)
                    .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("rawMaterialId", equalTo(rawMaterialId.intValue()))
                    .body("quantityRequired", equalTo(2.5f));
        }

        @Test
        void shouldThrowErrorWhenMaterialNotPresent(){
            given()
                    .contentType(ContentType.JSON)
                    .body("""
                        {
                            "quantityRequired": 2.5
                        }
                    """)
                    .when()
                    .post("/products/{productId}/composition", productId)
                    .then()
                    .statusCode(422);
        }

        @Test
        void shouldThrowErrorWhenQuantityNotPresent(){
            given()
                    .contentType(ContentType.JSON)
                    .body("""
                        {
                            "rawMaterialId": %d
                            }
                        """.formatted(rawMaterialId))
                    .then()
                    .statusCode(422);
        }
    }



    @Nested
    class UpdateComposition{
        @Test
        void shouldUpdateProductComposition(){
            System.out.println(compositionId);
            given()
                    .contentType(ContentType.JSON)
                    .body("""
                        {
                            "quantityRequired": 3.5
                        }
                        """)
                    .when()
                    .patch("/products/{productId}/composition/{id}",productId, compositionId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(compositionId.intValue()))
                    .body("rawMaterialId", equalTo(rawMaterialId.intValue()))
                    .body("rawMaterialName", equalTo("Matéria-Prima Teste"))
                    .body("quantityRequired", equalTo(3.5f));
        }
    }

}