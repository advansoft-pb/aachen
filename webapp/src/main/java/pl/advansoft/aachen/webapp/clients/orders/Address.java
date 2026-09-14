package pl.advansoft.aachen.webapp.clients.orders;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record Address(
        @NotBlank(message = "ZipCode is required") String zipCode,
        @NotBlank(message = "Country is required") String country)
        implements Serializable {
}
