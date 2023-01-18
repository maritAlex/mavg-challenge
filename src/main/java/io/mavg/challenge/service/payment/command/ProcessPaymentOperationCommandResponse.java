package io.mavg.challenge.service.payment.command;

import io.mavg.challenge.domain.entities.TransactionStatus;
import io.mavg.challenge.service.ICommandResponse;

import java.util.UUID;

public record ProcessPaymentOperationCommandResponse (
		UUID transactionId,
		AccountInformation accountOrigin,
		AccountInformation accountDestination,
		TransactionStatus status,
		String errorCode
) implements ICommandResponse {
}

