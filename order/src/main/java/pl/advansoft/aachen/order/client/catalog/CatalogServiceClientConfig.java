package pl.advansoft.aachen.order.client.catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import pl.advansoft.aachen.order.ApplicationProperties;

@Configuration
class CatalogServiceClientConfig {
    @Bean
    RestClient restClient(ApplicationProperties properties) {
        return RestClient
                .builder()
                .baseUrl(properties.catalogServiceUrl())
                .build();
    }
}
