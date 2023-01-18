package io.mavg.challenge.controller.api.operation.model;

import io.mavg.challenge.controller.api.account.model.AccountDto;
import io.mavg.challenge.domain.entities.TransactionStatus;

import java.util.UUID;

public record PaymentResponse(
		UUID transactionId,
		AccountDto originAccount,
		AccountDto destinationAccount,
		TransactionStatus status,
		String errorCode
) {
}
