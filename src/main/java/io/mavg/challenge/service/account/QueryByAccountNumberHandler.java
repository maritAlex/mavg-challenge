package io.mavg.challenge.service.account;

import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.service.IQueryHandler;
import io.mavg.challenge.service.account.query.QueryByAccountNumber;
import io.mavg.challenge.service.account.query.QueryByAccountNumberResponse;
import org.springframework.stereotype.Component;

@Component
public class QueryByAccountNumberHandler implements IQueryHandler<QueryByAccountNumber, QueryByAccountNumberResponse> {

	private final AccountRepository accountRepository;

	public QueryByAccountNumberHandler(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public QueryByAccountNumberResponse query(QueryByAccountNumber query) {
		var account = accountRepository.findByAccountNumber(query.accountNumber())
				.orElseThrow(() -> new IllegalArgumentException("not found"));

		return new QueryByAccountNumberResponse(
				account.getAccountHolderId(),
				account.getAccountNumber(),
				account.getBalance()
		);
	}
}
