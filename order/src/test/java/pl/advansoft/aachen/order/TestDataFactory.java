package pl.advansoft.aachen.order;

public class TestDataFactory {
    public static String createOrderRequestWithInvalidCustomer() {
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
}
