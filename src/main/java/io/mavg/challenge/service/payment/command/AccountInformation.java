package io.mavg.challenge.service.payment.command;

import java.math.BigDecimal;

public record AccountInformation(
		String accountNumber,
		String accountHolderId,
		BigDecimal balance
) {

}
