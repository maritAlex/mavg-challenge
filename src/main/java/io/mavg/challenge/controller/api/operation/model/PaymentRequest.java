package io.mavg.challenge.controller.api.operation.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PaymentRequest(
		@NotBlank
		String accountNumberOrigin,
		@NotBlank
		String accountNumberDestination,

		@DecimalMin(value = "0.0")
		@Digits(integer = 99, fraction = 2)
		BigDecimal amount
) {
}
