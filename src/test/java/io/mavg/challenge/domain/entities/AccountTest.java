package io.mavg.challenge.domain.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

	private Account account = new Account(
			1L,
			"holder",
			"22002",
			new BigDecimal(50)
	);

	@Test
	public void shouldHaveEnoughFunds() {
		var amountToCheck = new BigDecimal(10);

		assertTrue(account.hasEnoughFunds(amountToCheck));
	}

	@Test
	public void shouldNotHaveFunds() {
		var amountToCheck = new BigDecimal(100);

		assertFalse(account.hasEnoughFunds(amountToCheck));
	}

	@Test
	public void shouldDebitAnAmountGiven() {
		var amountToDebit = new BigDecimal(20);
		var expectedBalanceAfterDebit = new BigDecimal(30);
		var account = new Account(
				1L,
				"holder",
				"22002",
				new BigDecimal(50)
		);

		account.debit(amountToDebit);

		assertEquals(expectedBalanceAfterDebit, account.getBalance());
	}

	@Test
	public void shouldCreditAnAmountGiven() {
		var amountToCredit = new BigDecimal(20);
		var expectedBalanceAfterDebit = new BigDecimal(70);
		var account = new Account(
				1L,
				"holder",
				"22002",
				new BigDecimal(50)
		);

		account.credit(amountToCredit);

		assertEquals(expectedBalanceAfterDebit, account.getBalance());
	}

	@Test
	public void shouldCreateANewAccountWithAccountNumberOfLengthTen() {
		var holder = "holder";
		var account = new Account(holder);

		assertEquals(holder, account.getAccountHolderId());
		assertNotNull(account.getAccountNumber());
		assertEquals(10, account.getAccountNumber().length());
	}

}