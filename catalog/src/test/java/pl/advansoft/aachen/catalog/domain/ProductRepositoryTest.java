package pl.advansoft.aachen.catalog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import pl.advansoft.aachen.catalog.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.test.database.replace=none")
@Import(TestcontainersConfiguration.class)
@Sql("/test-data.sql")
class ProductRepositoryTest {

    private final ProductRepository productRepository;

    ProductRepositoryTest(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(5);
    }

    @Test
    void shouldGetProduct() {
        ProductEntity product = productRepository.findByCode("P3");
        assertThat(product.getCode()).isEqualTo("P3");
        assertThat(product.getName()).isEqualTo("The Hunger Games 3");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(17.99d));
    }
}