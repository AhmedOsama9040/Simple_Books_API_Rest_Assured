import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BookTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @Test
    public void getFictionBooks() {
        given()
                .queryParam("type", "fiction")
                .queryParam("limit", 4)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .time(lessThan(2000L))
                .body("type", everyItem(equalTo("fiction")))
                .body("size()", is(4));
    }

    @Test
    public void getBooksInvalidScenario() {
        given()
                .queryParam("type", "crime")
                .when()
                .get("/books")
                .then()
                .statusCode(400)
                .contentType("application/json")
                .time(lessThan(2000L))
                .body("error", notNullValue());
    }

    @Test
    public void getSingleBook() {
        int bookId = 2; // Example book ID

        given()
                .pathParam("bookId", bookId)
                .when()
                .get("/books/{bookId}")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .time(lessThan(2000L))
                .body("id", equalTo(bookId))
                .body("$", hasKey("name"))
                .body("$", hasKey("author"))
                .body("$", hasKey("type"))
                .body("$", hasKey("price"))
                .body("$", hasKey("current-stock"))
                .body("$", hasKey("available"));
    }
}

