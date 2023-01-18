package io.mavg.challenge.service.account;

import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.service.ICommand;
import io.mavg.challenge.service.ICommandHandler;
import io.mavg.challenge.service.account.command.UpdateAccountBalanceCommand;
import io.mavg.challenge.service.account.command.UpdateAccountBalanceCommandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateAccountBalanceCommandHandler implements ICommandHandler<UpdateAccountBalanceCommand, UpdateAccountBalanceCommandResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAccountBalanceCommandHandler.class);

	private final AccountRepository accountRepository;

	public UpdateAccountBalanceCommandHandler(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UpdateAccountBalanceCommandResponse handle(UpdateAccountBalanceCommand command) {
		LOGGER.info("Updating account balance. Account number {}", command.accountNumber());

		var account = accountRepository.findByAccountNumber(command.accountNumber())
				.orElseThrow(() -> new IllegalArgumentException("account_not_found"));

		account.setBalance(command.amount());

		accountRepository.save(account);

		return new UpdateAccountBalanceCommandResponse(
				account.getAccountNumber(),
				account.getAccountHolderId(),
				account.getBalance()
		);
	}
}
