package pl.advansoft.aachen.order.domain;

import org.junit.jupiter.api.Test;
import pl.advansoft.aachen.order.AbstractIT;
import pl.advansoft.aachen.order.WithMockOAuth2User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetOrdersTest extends AbstractIT {

    @Test
    @WithMockOAuth2User(username = "user")
    void shouldGetOrdersSuccessfully() throws Exception {
        mockMvc
                .perform(get("/api/orders"))
                .andExpect(status().isOk());
    }
}
