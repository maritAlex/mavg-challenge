package io.mavg.challenge.controller.api.account.model;

import jakarta.validation.constraints.NotBlank;

public record NewAccountRequest(
		@NotBlank
		String accountHolder
) {
}
