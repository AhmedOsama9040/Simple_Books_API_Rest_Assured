import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrderTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @Test
    public void submitOrderWithoutAuth() {
        given()
                .contentType("application/json")
                .body("{\"bookId\": 1, \"customerName\": \"Ahmed\"}")
                .when()
                .post("/orders")
                .then()
                .statusCode(401)
                .contentType("application/json")
                .body("error", equalTo("Missing Authorization header."));
    }

    @Test(dependsOnMethods = "apiAuthentication")
    public void submitOrderWithAuth() {
        String response =
                given()
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + Config.token)
                        .body("{\"bookId\": 5, \"customerName\": \"Ahmed\"}")
                        .when()
                        .post("/orders")
                        .then()
                        .statusCode(201)
                        .time(lessThan(2000L))
                        .body("created", equalTo(true)).extract().response().asString();
        JsonPath responseJson = new JsonPath(response);
        Config.orderId = responseJson.getString("orderId");
    }

    @Test(dependsOnMethods = "submitOrderWithAuth")
    public void getOrder() {
        String orderId = Config.orderId; // Replace with actual order ID

        given()
                .pathParam("orderId", orderId)
                .header("Authorization", "Bearer " + Config.token)
                .when()
                .get("/orders/{orderId}")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", equalTo(orderId));
    }

    @Test(dependsOnMethods = "submitOrderWithAuth")
    public void updateOrder() {
        String orderId = Config.orderId; // Replace with actual order ID

        given()
                .pathParam("orderId", orderId)
                .header("Authorization", "Bearer " + Config.token)
                .contentType("application/json")
                .body("{\"customerName\": \"Osama\"}")
                .when()
                .patch("/orders/{orderId}")
                .then()
                .statusCode(204)
                .time(lessThan(2000L));
    }

    @Test(dependsOnMethods = "submitOrderWithAuth")
    public void deleteOrder() {
        String orderId = Config.orderId; // Replace with actual order ID

        given()
                .pathParam("orderId", orderId)
                .header("Authorization", "Bearer " + Config.token)
                .when()
                .delete("/orders/{orderId}")
                .then()
                .statusCode(204)
                .time(lessThan(2000L));
    }
}

