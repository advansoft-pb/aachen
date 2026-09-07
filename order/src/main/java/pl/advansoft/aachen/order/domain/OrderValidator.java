package pl.advansoft.aachen.order.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.advansoft.aachen.order.client.catalog.Product;
import pl.advansoft.aachen.order.client.catalog.ProductServiceClient;
import pl.advansoft.aachen.order.domain.models.CreateOrderRequest;
import pl.advansoft.aachen.order.domain.models.OrderItem;

import java.util.Set;

@Component
public class OrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient client;

    OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest request) {

        Set<OrderItem> items = request.items();

        for (OrderItem item : items) {

            Product product = client
                    .getProductByCode(item.code())
                    .orElseThrow(() -> InvalidOrderException.forCode(item.code()));

            if (item.price().compareTo(product.price()) != 0) {
                LOGGER.error("Product price not matching. Actual price: {}, received price: {}",
                        product.price(), item.price());
                throw InvalidOrderException.forPrice();
            }
        }
    }
}
