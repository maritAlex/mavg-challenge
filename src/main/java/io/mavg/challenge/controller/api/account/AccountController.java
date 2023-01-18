package io.mavg.challenge.controller.api.account;

import io.mavg.challenge.controller.api.account.model.AccountDto;
import io.mavg.challenge.controller.api.account.model.NewAccountRequest;
import io.mavg.challenge.controller.api.account.model.PatchBalanceRequest;
import io.mavg.challenge.service.ICommandHandler;
import io.mavg.challenge.service.IQueryHandler;
import io.mavg.challenge.service.account.command.CreateAccountCommand;
import io.mavg.challenge.service.account.command.CreatedAccountCommandResponse;
import io.mavg.challenge.service.account.command.UpdateAccountBalanceCommand;
import io.mavg.challenge.service.account.command.UpdateAccountBalanceCommandResponse;
import io.mavg.challenge.service.account.query.QueryByAccountNumber;
import io.mavg.challenge.service.account.query.QueryByAccountNumberResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private final IQueryHandler<QueryByAccountNumber, QueryByAccountNumberResponse> queryByAccountId;
	private final ICommandHandler<CreateAccountCommand, CreatedAccountCommandResponse> createAccountCommandHandler;

	private final ICommandHandler<UpdateAccountBalanceCommand, UpdateAccountBalanceCommandResponse> updateAccountBalanceCommandHandler;

	public AccountController(
			IQueryHandler<QueryByAccountNumber, QueryByAccountNumberResponse> queryByAccountId,
			ICommandHandler<CreateAccountCommand, CreatedAccountCommandResponse> createAccountCommandHandler,
			ICommandHandler<UpdateAccountBalanceCommand, UpdateAccountBalanceCommandResponse> updateAccountBalanceCommandHandler
	) {
		this.queryByAccountId = queryByAccountId;
		this.createAccountCommandHandler = createAccountCommandHandler;
		this.updateAccountBalanceCommandHandler = updateAccountBalanceCommandHandler;
	}

	@GetMapping("/{accountNumber}")
	public AccountDto getAccount(
			@PathVariable String accountNumber
	) {
		LOGGER.info("Getting account number: {}", accountNumber);
		var accountResponse = queryByAccountId.query(new QueryByAccountNumber(accountNumber));

		return new AccountDto(
				accountResponse.accountHolder(),
				accountResponse.accountNumber(),
				accountResponse.balance()
		);
	}

	@PostMapping
	public AccountDto createAccount(
			@Valid @RequestBody NewAccountRequest newAccountRequest
	) {
		LOGGER.info("Creating account for account Holder: {}", newAccountRequest.accountHolder());
		var accountCreatedResponse = createAccountCommandHandler.handle(
				new CreateAccountCommand(newAccountRequest.accountHolder())
		);

		return new AccountDto(
				accountCreatedResponse.accountHolder(),
				accountCreatedResponse.accountNumber(),
				accountCreatedResponse.balance()
		);
	}

	@PatchMapping("/{accountNumber}")
	public AccountDto patchBalance(
			@PathVariable String accountNumber,
			@Valid @RequestBody PatchBalanceRequest patchBalanceRequest
	) {
		LOGGER.info("Patching balance of account: {}", accountNumber);

		var accountPatched = updateAccountBalanceCommandHandler.handle(
				new UpdateAccountBalanceCommand(
						accountNumber,
						patchBalanceRequest.amount()
				)
		);

		return new AccountDto(
				accountPatched.accountHolderId(),
				accountPatched.accountNumber(),
				accountPatched.balance()
		);
	}
}
