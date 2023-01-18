package io.mavg.challenge.controller.api.account.model;

import java.math.BigDecimal;

public record AccountDto(
		String accountHolder,
		String accountNumber,
		BigDecimal balance
) {
}
