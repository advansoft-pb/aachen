package pl.advansoft.aachen.order.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank String zipCode,
        @NotBlank String country) {
}
