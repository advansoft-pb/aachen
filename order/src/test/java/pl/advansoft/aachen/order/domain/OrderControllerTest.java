package pl.advansoft.aachen.order.domain;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import pl.advansoft.aachen.order.AbstractIT;
import pl.advansoft.aachen.order.TestDataFactory;
import pl.advansoft.aachen.order.domain.models.OrderSummary;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql("/test-orders.sql")
class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            String payload = TestDataFactory.createSimplePayload();
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
            String payload = TestDataFactory.createStringOrderRequestWithInvalidCustomer();
            given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrdersTests {
        @Test
        void shouldGetOrdersSuccessfully() {
            List<OrderSummary> orderSummaries = given()
                    .when()
                    .get("/api/orders")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .as(new TypeRef<>() {
                    });

            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        final String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given()
                    .when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
