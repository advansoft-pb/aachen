package pl.advansoft.aachen.catalog;

import org.springframework.boot.SpringApplication;

public class TestCatalogApplication {

	public static void main(final String... args) {
		SpringApplication
				.from(CatalogApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}
}
