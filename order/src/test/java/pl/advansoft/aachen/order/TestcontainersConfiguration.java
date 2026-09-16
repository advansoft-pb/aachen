package pl.advansoft.aachen.order;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    static final String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.6.1";
    static final String REALM_IMPORT_FILE = "/bookstore-realm.json";
    static final String REALM_NAME = "bookstore";

    static final WireMockContainer WIREMOCK_SERVER = new WireMockContainer("wiremock/wiremock:3.13.2-alpine");

    @Bean
    WireMockContainer wiremockServer() {
        WIREMOCK_SERVER.start();
        configureFor(WIREMOCK_SERVER.getHost(), WIREMOCK_SERVER.getPort());
        return WIREMOCK_SERVER;
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:18-alpine"));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.3.5-alpine"));
    }

    @Bean
    KeycloakContainer keycloak() {
        return new KeycloakContainer(KEYCLOAK_IMAGE).withRealmImportFile(REALM_IMPORT_FILE);
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar(WireMockContainer wiremockServer, KeycloakContainer keycloak) {
        return registry -> {
            registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                    () -> keycloak.getAuthServerUrl() + "/realms/" + REALM_NAME);
        };
    }
}
