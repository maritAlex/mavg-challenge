package io.mavg.challenge.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accountHolderId;

	private String accountNumber;

	private BigDecimal balance;

	public Account() {}

	public Account(Long id, String accountHolderId, String accountNumber, BigDecimal balance) {
		this.id = id;
		this.accountHolderId = accountHolderId;
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public Account(String accountHolderId) {
		this(
				null,
				accountHolderId,
				newAccountNumber(),
				BigDecimal.ZERO
		);
	}

	private static String newAccountNumber() {
		return RandomStringUtils.randomNumeric(10);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountHolderId() {
		return accountHolderId;
	}

	public void setAccountHolderId(String accountHolderId) {
		this.accountHolderId = accountHolderId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean hasEnoughFunds(BigDecimal amount) {
		return this.balance.compareTo(amount) >= 0;
	}

	public BigDecimal debit(BigDecimal amount) {
		this.balance = this.balance.subtract(amount);
		return this.balance;
	}

	public BigDecimal credit(BigDecimal amount) {
		this.balance = this.balance.add(amount);
		return this.balance;
	}
}
