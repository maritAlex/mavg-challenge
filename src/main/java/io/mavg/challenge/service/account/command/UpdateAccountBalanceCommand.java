package io.mavg.challenge.service.account.command;

import io.jsonwebtoken.lang.Assert;
import io.mavg.challenge.service.ICommand;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public record UpdateAccountBalanceCommand(
		String accountNumber,
		BigDecimal amount
) implements ICommand {

	public UpdateAccountBalanceCommand {
		Assert.isTrue(StringUtils.isNotBlank(accountNumber));
		Assert.isTrue(amount.compareTo(BigDecimal.ZERO) >= 0);
	}
}
