package io.mavg.challenge.service.account.query;

import io.mavg.challenge.service.IQueryResponse;

import java.math.BigDecimal;

public record QueryByAccountNumberResponse(
		String accountHolder,
		String accountNumber,
		BigDecimal balance
) implements IQueryResponse { }
