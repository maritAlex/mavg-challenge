package io.mavg.challenge.service.payment.command;

import io.jsonwebtoken.lang.Assert;
import io.mavg.challenge.service.ICommand;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public record ProcessPaymentOperationCommand(
		String accountNumberOrigin,
		String accountNumberDestination,
		BigDecimal amount,
		String username
) implements ICommand {

	public ProcessPaymentOperationCommand {
		Assert.isTrue(StringUtils.isNotBlank(accountNumberOrigin));
		Assert.isTrue(StringUtils.isNotBlank(accountNumberDestination));
		Assert.isTrue(amount.compareTo(BigDecimal.ZERO) >= 0);
		Assert.isTrue(StringUtils.isNotBlank(username));
	}
}
