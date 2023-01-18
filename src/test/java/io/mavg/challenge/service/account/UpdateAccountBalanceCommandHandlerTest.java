package io.mavg.challenge.service.account;

import io.mavg.challenge.domain.entities.Account;
import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.service.account.command.UpdateAccountBalanceCommand;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdateAccountBalanceCommandHandlerTest {

	private AccountRepository accountRepository = mock(AccountRepository.class);

	private UpdateAccountBalanceCommandHandler commandHandler = new UpdateAccountBalanceCommandHandler(
			accountRepository
	);

	@Test
	public void shouldThrowAnExceptionWhenTheAccountDoesNotExists() {

		var accountNumberOrigin = "12345";

		when(accountRepository.findByAccountNumber(accountNumberOrigin)).thenReturn(Optional.empty());

		assertThrows(
				IllegalArgumentException.class,
				() -> commandHandler.handle(
						new UpdateAccountBalanceCommand(
								"1234",
								new BigDecimal(50)
						)
				)
		);

	}

	@Test
	public void shouldUpdateAccountBalanceWhenIsSuccess(){

		var account = "1234";
		var balance = new BigDecimal(100);
		var amount = new BigDecimal(50);

		when(accountRepository.findByAccountNumber(account)).thenReturn(Optional.of(new Account(
				1L,
				"001",
				account,
				balance
		)));

		when(accountRepository.save(new Account(
				1L,
				"001",
				account,
				amount))).thenReturn(any());

		var response = commandHandler.handle(new UpdateAccountBalanceCommand(
				account,
				amount
		));

		assertEquals(amount, response.balance());

	}

}