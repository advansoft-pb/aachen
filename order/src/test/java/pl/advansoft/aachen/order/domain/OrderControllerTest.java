package pl.advansoft.aachen.order.domain;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.advansoft.aachen.order.AbstractIT;
import pl.advansoft.aachen.order.TestDataFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            String payload = """
                    {
                      "customer": {
                        "name": "Stefan",
                        "phone": "1234567890"
                      },
                      "deliveryAddress": {
                        "zipCode": "12345",
                        "country": "Polska"
                      },
                      "items": [
                        {
                          "name": "hej",
                          "code": "P23",
                          "price": 0.40,
                          "quantity": 4
                        }
                      ]
                    }
                    """;
            given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenManadatoryDataIsMissing() {
            String payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}