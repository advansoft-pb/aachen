package pl.advansoft.aachen.order;

import org.instancio.Instancio;
import pl.advansoft.aachen.order.domain.models.Address;
import pl.advansoft.aachen.order.domain.models.CreateOrderRequest;
import pl.advansoft.aachen.order.domain.models.Customer;

import java.util.Set;

import static org.instancio.Select.field;

public class TestDataFactory {
    public static String createStringOrderRequestWithInvalidCustomer() {
        return """
                {
                  "customer": {
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
    }

    public static CreateOrderRequest createOrderRequest() {
        return Instancio.of(CreateOrderRequest.class).create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        return Instancio
                .of(CreateOrderRequest.class)
                .ignore(field(Customer::name))
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidAddress() {
        return Instancio
                .of(CreateOrderRequest.class)
                .set(field(Address::zipCode), "")
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        return Instancio
                .of(CreateOrderRequest.class)
                .set(field(CreateOrderRequest::items), Set.of())
                .create();
    }
}
