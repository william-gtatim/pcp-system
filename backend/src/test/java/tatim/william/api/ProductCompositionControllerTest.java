package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


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
                        .post("/products/{productId}/compositions", productId)
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
                    .post("/products/{productId}/compositions", productId)
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
                    .post("/products/{productId}/compositions", productId)
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
                    .patch("/products/{productId}/compositions/{id}",productId, compositionId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(compositionId.intValue()))
                    .body("rawMaterialId", equalTo(rawMaterialId.intValue()))
                    .body("rawMaterialName", equalTo("Matéria-Prima Teste"))
                    .body("quantityRequired", equalTo(3.5f));
        }
    }


    @Nested
    class ListProductComposition{

        @Test
        void shouldReturnAListOfCompositions(){
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/products/{productId}/compositions", productId)
                    .then()
                    .statusCode(200)
                    .body("", not(empty()))
                    .body("size()",equalTo(1))
                    .body("[0].id", notNullValue())
                    .body("[0].productId", equalTo(productId.intValue()))
                    .body("[0].rawMaterialId", notNullValue())
                    .body("[0].rawMaterialName", equalTo("Matéria-Prima Teste"))
                    .body("[0].quantityRequired", equalTo(2.5f));
        }

        @Test
        void shouldReturnNotFoundWhenProductNotExists(){
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/products/{productId}/compositions", 2L)
                    .then()
                    .statusCode(404);

        }
    }

}