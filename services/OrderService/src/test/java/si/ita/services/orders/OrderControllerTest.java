package si.ita.services.orders;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class OrderControllerTest {
    @Test
    public void testGetAllOrdersEndpoint() {
        // check if response is 200 and in body is array with size is greater than 0
        given()
                .when().get("/orders")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetOrderById() {
        // check if response code is 200 and body is json with id = 6426a88bb14ce31e7fdaf6f7
        given()
                .when().get("/orders/6426a88bb14ce31e7fdaf6f7")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo("6426a88bb14ce31e7fdaf6f7"));
    }

    @Test
    public void testGetUserOrders() {
        // check if response code is 200
        given()
                .when().get("/orders/owned/641b3a3adbf3f799986399f5")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(1))
                .body("[0].quantity", equalTo(1))
                .body("[0].price", equalTo(22.0f));
    }

}