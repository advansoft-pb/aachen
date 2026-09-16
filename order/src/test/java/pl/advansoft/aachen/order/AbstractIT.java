package pl.advansoft.aachen.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureWebMvc
public abstract class AbstractIT {

    static final String CLIENT_ID = "bookstore-webapp";
    static final String CLIENT_SECRET = "P1sibsIrELBhmvK18BOzw1bUl96DcP2z";
    static final String USERNAME = "siva";
    static final String PASSWORD = "siva1234";

    @Autowired
    OAuth2ResourceServerProperties properties;

    @LocalServerPort
    int port;

    protected static void mockGetProductByCode(String code, BigDecimal price) {
        stubFor(WireMock
                .get(urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(
                                """
                                        {
                                            "code": "%s",
                                            "price": "%f"
                                        }
                                        """.formatted(code, price.doubleValue()))));
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected String getToken() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(OAuth2Constants.GRANT_TYPE, singletonList(OAuth2Constants.PASSWORD));
        map.put(OAuth2Constants.CLIENT_ID, singletonList(CLIENT_ID));
        map.put(OAuth2Constants.CLIENT_SECRET, singletonList(CLIENT_SECRET));
        map.put(OAuth2Constants.USERNAME, singletonList(USERNAME));
        map.put(OAuth2Constants.PASSWORD, singletonList(PASSWORD));

        String authServerUrl = properties.getJwt().getIssuerUri() + "/protocol/openid-connect/token";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        KeyCloakToken token = template.postForObject(authServerUrl, request, KeyCloakToken.class);

        assert token != null;

        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
    }
}
