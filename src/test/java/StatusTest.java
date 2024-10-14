import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class StatusTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @Test
    public void checkAPIStatus() {
        given()
                .when()
                .get("/status")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .time(lessThan(2000L))
                .body("status", equalTo("OK"));
    }
}
