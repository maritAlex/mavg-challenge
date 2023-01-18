package io.mavg.challenge.service.account.query;


import io.mavg.challenge.service.IQuery;

import java.util.Objects;

public record QueryByAccountNumber(String accountNumber) implements IQuery {
	public QueryByAccountNumber {
		Objects.requireNonNull(accountNumber);
	}

}
