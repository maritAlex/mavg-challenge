package io.mavg.challenge.service.account.command;

import io.mavg.challenge.service.ICommandResponse;

import java.math.BigDecimal;

public record CreatedAccountCommandResponse(
		String accountHolder,
		String accountNumber,
		BigDecimal balance
) implements ICommandResponse {
}
