package io.mavg.challenge.service.account.command;

import io.mavg.challenge.service.ICommandResponse;

import java.math.BigDecimal;

public record UpdateAccountBalanceCommandResponse(
		String accountNumber,
		String accountHolderId,
		BigDecimal balance
) implements ICommandResponse {
}
