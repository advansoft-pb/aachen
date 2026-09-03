package pl.advansoft.aachen.order.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "Customer Name is required") String name,
        @NotBlank(message = "Customer Phone Number is required") String phone) {
}
