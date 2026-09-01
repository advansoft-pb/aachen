package pl.advansoft.aachen.order;

import org.springframework.boot.SpringApplication;

public class TestOrderApplication {

	public static void main(final String... args) {
		SpringApplication
				.from(OrderApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}
}
