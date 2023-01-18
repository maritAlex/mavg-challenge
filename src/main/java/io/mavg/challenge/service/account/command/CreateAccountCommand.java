package io.mavg.challenge.service.account.command;

import io.jsonwebtoken.lang.Assert;
import io.mavg.challenge.service.ICommand;
import org.apache.commons.lang3.StringUtils;

public record CreateAccountCommand(
		String accountHolderId
) implements ICommand {

	public CreateAccountCommand {
		Assert.isTrue(StringUtils.isNotBlank(accountHolderId));
	}

}
