import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.lessThan;

public class Authentication {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @Test
    public void apiAuthentication() {
        Response response = given()
                .contentType("application/json")
                .body("{\"clientName\": \"Ahmed\", \"clientEmail\": \"osmanabhvgghasdgf@example.com\"}")
                .when()
                .post("/api-clients/");

        response.then()
                .statusCode(201)
                .time(lessThan(2000L));

        // Extract token from response
        Config.token = response.jsonPath().getString("accessToken");
    }
}

