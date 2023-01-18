package io.mavg.challenge.service.account;

import io.mavg.challenge.domain.entities.Account;
import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.service.account.command.CreateAccountCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAccountCommandHandlerTest {

	private AccountRepository accountRepository = mock(AccountRepository.class);

	private CreateAccountCommandHandler commandHandler = new CreateAccountCommandHandler(
			accountRepository
	);

	@Test
	public void shouldCreateAnAccountGivenAnAccountHolder() {
		var accountHolder = "holder";
		var accountCreated = new Account(accountHolder);
		accountCreated.setId(1L);

		when(accountRepository.save(any())).thenReturn(accountCreated);

		var response = commandHandler.handle(
				new CreateAccountCommand(accountHolder)
		);

		assertEquals(accountHolder, response.accountHolder());
		assertEquals(BigDecimal.ZERO, response.balance());
		assertNotNull(response.accountNumber());
	}

}