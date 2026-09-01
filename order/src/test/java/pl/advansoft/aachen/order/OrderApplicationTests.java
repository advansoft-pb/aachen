package pl.advansoft.aachen.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderApplicationTests {

	@Test
	void contextLoads() {
	}
}
