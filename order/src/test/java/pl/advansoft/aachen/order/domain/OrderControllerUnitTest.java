package pl.advansoft.aachen.order.domain;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.advansoft.aachen.order.TestDataFactory;
import pl.advansoft.aachen.order.domain.models.CreateOrderRequest;
import pl.advansoft.aachen.order.domain.models.CreateOrderResponse;
import pl.advansoft.aachen.order.domain.models.OrderItem;
import tools.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerUnitTest {

    private static final String USER = "piotr";
    private static final String NUMBER = "1234";

    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private SecurityService securityService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(named("Order with Invalid Customer", TestDataFactory.createOrderRequestWithInvalidCustomer())),
                arguments(named("Order with Invalid Delivery Address", TestDataFactory.createOrderRequestWithInvalidAddress())),
                arguments(named("Order with Invalid No Items", TestDataFactory.createOrderRequestWithNoItems()))
        );
    }

    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn(USER);
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        mockMvc
                .perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnProperRequestWhenOrderPayloadIsValid() throws Exception {
        given(orderService.createOrder(eq(USER), any(CreateOrderRequest.class))).willReturn(new CreateOrderResponse(NUMBER));

        mockMvc
                .perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestDataFactory.createOrderRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").isNotEmpty());
    }

    @Test
    void testJson() {
        CreateOrderResponse response = new CreateOrderResponse(NUMBER);
        String actual = objectMapper.writeValueAsString(response);
        String orderNumber = JsonPath.read(actual, "$.orderNumber");
        assertThat(orderNumber).isNotBlank();
    }

    @Test
    void test() {
        CreateOrderRequest req = TestDataFactory.createOrderRequest();

        System.out.println("name = " + req.customer().name());
        System.out.println("phone = " + req.customer().phone());
        System.out.println("country = " + req.deliveryAddress().country());
        System.out.println("zipCode = " + req.deliveryAddress().zipCode());
        System.out.println("size = " + req.items().size());

        Iterator<OrderItem> iterator = req.items().iterator();

        if (iterator.hasNext()) {
            OrderItem item = iterator.next();
            System.out.println("code = " + item.code());
            System.out.println("name = " + item.name());
            System.out.println("price = " + item.price());
            System.out.println("quantity = " + item.quantity());
        }

        System.out.println("-------------------------------------------");

        String json = objectMapper.writeValueAsString(req);
        System.out.println("json = " + json);
    }
}
