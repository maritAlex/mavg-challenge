package io.mavg.challenge.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactional_logs")
public class TransactionalLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private UUID transactionId;

	@Enumerated(EnumType.STRING)
	private TransactionStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_origin_id")
	private Account accountOrigin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_destination_id")
	private Account accountDestination;

	private BigDecimal accountOriginBalance;

	private BigDecimal accountDestinationBalance;

	private BigDecimal transactionAmount;

	private Instant createdTime;

	private String username;

	public TransactionalLog() {}

	public TransactionalLog(
			Account accountOrigin,
			Account accountDestination,
			BigDecimal accountOriginBalance,
			BigDecimal accountDestinationBalance,
			BigDecimal transactionAmount,
			String username,
			TransactionStatus status
	) {
		this.accountOrigin = accountOrigin;
		this.accountDestination = accountDestination;
		this.accountOriginBalance = accountOriginBalance;
		this.accountDestinationBalance = accountDestinationBalance;
		this.transactionAmount = transactionAmount;
		this.username = username;
		this.status = status;
		this.createdTime = Instant.now();
		this.transactionId = UUID.randomUUID();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public Account getAccountOrigin() {
		return accountOrigin;
	}

	public void setAccountOrigin(Account accountOrigin) {
		this.accountOrigin = accountOrigin;
	}

	public Account getAccountDestination() {
		return accountDestination;
	}

	public void setAccountDestination(Account accountDestination) {
		this.accountDestination = accountDestination;
	}

	public BigDecimal getAccountOriginBalance() {
		return accountOriginBalance;
	}

	public void setAccountOriginBalance(BigDecimal accountOriginBalance) {
		this.accountOriginBalance = accountOriginBalance;
	}

	public BigDecimal getAccountDestinationBalance() {
		return accountDestinationBalance;
	}

	public void setAccountDestinationBalance(BigDecimal accountDestinationBalance) {
		this.accountDestinationBalance = accountDestinationBalance;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Instant getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
