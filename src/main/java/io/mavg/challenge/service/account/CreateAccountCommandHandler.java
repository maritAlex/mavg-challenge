package io.mavg.challenge.service.account;

import io.mavg.challenge.domain.entities.Account;
import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.service.ICommandHandler;
import io.mavg.challenge.service.account.command.CreateAccountCommand;
import io.mavg.challenge.service.account.command.CreatedAccountCommandResponse;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommandHandler implements ICommandHandler<CreateAccountCommand, CreatedAccountCommandResponse> {

	private final AccountRepository accountRepository;

	public CreateAccountCommandHandler(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public CreatedAccountCommandResponse handle(CreateAccountCommand command) {
		var account = new Account(
				command.accountHolderId()
		);

		var accountCreated = accountRepository.save(account);

		return new CreatedAccountCommandResponse(
				accountCreated.getAccountHolderId(),
				accountCreated.getAccountNumber(),
				accountCreated.getBalance()
		);
	}
}
