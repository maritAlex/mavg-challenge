package io.mavg.challenge.service.payment;

import io.mavg.challenge.domain.entities.Account;
import io.mavg.challenge.domain.entities.TransactionStatus;
import io.mavg.challenge.domain.entities.TransactionalLog;
import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.repository.transactionallog.TransactionalLogRepository;
import io.mavg.challenge.service.ICommandHandler;
import io.mavg.challenge.service.payment.command.AccountInformation;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommand;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessPaymentOperationCommandHandler implements ICommandHandler<ProcessPaymentOperationCommand, ProcessPaymentOperationCommandResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentOperationCommandHandler.class);

	private final AccountRepository accountRepository;
	private final TransactionalLogRepository transactionalLogRepository;

	public ProcessPaymentOperationCommandHandler(AccountRepository accountRepository, TransactionalLogRepository transactionalLogRepository) {
		this.accountRepository = accountRepository;
		this.transactionalLogRepository = transactionalLogRepository;
	}

	@Override
	public ProcessPaymentOperationCommandResponse handle(ProcessPaymentOperationCommand command) {
		var accountOrigin = accountRepository.findByAccountNumber(command.accountNumberOrigin())
				.orElseThrow(() -> new IllegalArgumentException("account_origin_not_found"));

		var accountDestination = accountRepository.findByAccountNumber(command.accountNumberDestination())
				.orElseThrow(() -> new IllegalArgumentException("account_destination_not_found"));

		if(!accountOrigin.hasEnoughFunds(command.amount())) {
			var transactionLog = createLogTransaction(
					accountOrigin,
					accountDestination,
					command.amount(),
					command.username(),
					TransactionStatus.INSUFFICIENT_FUNDS
			);

			return getProcessPaymentOperationCommandResponse(
					transactionLog,
					accountOrigin,
					accountDestination,
					TransactionStatus.INSUFFICIENT_FUNDS,
					"insufficient_funds_origin_account"
			);
		}

		try {
			accountOrigin.debit(command.amount());
			accountDestination.credit(command.amount());

			accountRepository.save(accountOrigin);
			accountRepository.save(accountDestination);

			var transactionalLog = createLogTransaction(
					accountOrigin,
					accountDestination,
					command.amount(),
					command.username(),
					TransactionStatus.SUCCESS
			);

			return getProcessPaymentOperationCommandResponse(
					transactionalLog,
					accountOrigin,
					accountDestination,
					TransactionStatus.SUCCESS,
					null
			);
		} catch (Exception ex) {
			LOGGER.error("Error processing payment origin: {}, destination: {}, user: {}",
					accountOrigin.getAccountNumber(),
					accountDestination.getAccountNumber(),
					command.username()
			);

			var transactionalLog = createLogTransaction(
					accountOrigin,
					accountDestination,
					command.amount(),
					command.username(),
					TransactionStatus.FAIL
			);

			return getProcessPaymentOperationCommandResponse(
					transactionalLog,
					accountOrigin,
					accountDestination,
					TransactionStatus.FAIL,
					"processing_error"
			);
		}
	}

	private static ProcessPaymentOperationCommandResponse getProcessPaymentOperationCommandResponse(
			TransactionalLog transactionalLog,
			Account accountOrigin,
			Account accountDestination,
			TransactionStatus status,
			String errorCode
	) {
		return new ProcessPaymentOperationCommandResponse(
				transactionalLog.getTransactionId(),
				new AccountInformation(
						accountOrigin.getAccountNumber(),
						accountOrigin.getAccountHolderId(),
						accountOrigin.getBalance()
				),
				new AccountInformation(
						accountDestination.getAccountNumber(),
						accountDestination.getAccountHolderId(),
						accountDestination.getBalance()
				),
				status,
				errorCode
		);
	}

	private TransactionalLog createLogTransaction(
			Account origin,
			Account destination,
			BigDecimal amount,
			String username,
			TransactionStatus status
	) {
		return transactionalLogRepository.save(
				new TransactionalLog(
						origin,
						destination,
						origin.getBalance(),
						destination.getBalance(),
						amount,
						username,
						status
				)
		);
	}
}
