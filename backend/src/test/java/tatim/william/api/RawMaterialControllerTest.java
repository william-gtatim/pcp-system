package tatim.william.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@QuarkusTest
class RawMaterialControllerTest {

    @Test
    void shouldCreateUpdateListDeleteGetRawMaterial(){
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

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "name": "Nome atualizado",
                            "stockQuantity": 20
                        }
                        """)
                .when()
                .put("/raw-materials/{id}", rawMaterialId)
                .then()
                .statusCode(200)
                .body("id", equalTo(rawMaterialId.intValue()))
                .body("code", startsWith("MP-"))
                .body("name", equalTo("Nome atualizado"))
                .body("stockQuantity", equalTo(20));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/raw-materials/{id}", rawMaterialId)
                .then()
                .statusCode(200)
                .body("id", equalTo(rawMaterialId.intValue()))
                .body("code", startsWith("MP-"))
                .body("name", equalTo("Nome atualizado"))
                .body("stockQuantity", equalTo(20));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/raw-materials")
                .then()
                .statusCode(200)
                .body("", isA(List.class))
                .body("", not(empty()));

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/raw-materials/{id}", rawMaterialId)
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/raw-materials/{id}", rawMaterialId)
                .then()
                .statusCode(404);
    }
}