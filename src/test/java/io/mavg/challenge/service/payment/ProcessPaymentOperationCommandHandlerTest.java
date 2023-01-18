package io.mavg.challenge.service.payment;

import io.mavg.challenge.domain.entities.Account;
import io.mavg.challenge.domain.entities.TransactionStatus;
import io.mavg.challenge.domain.entities.TransactionalLog;
import io.mavg.challenge.repository.account.AccountRepository;
import io.mavg.challenge.repository.transactionallog.TransactionalLogRepository;
import io.mavg.challenge.service.payment.command.ProcessPaymentOperationCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProcessPaymentOperationCommandHandlerTest {

    private AccountRepository accountRepository = mock(AccountRepository.class);

    private TransactionalLogRepository transactionalLogRepository = mock(TransactionalLogRepository.class);

    private ProcessPaymentOperationCommandHandler commandHandler = new ProcessPaymentOperationCommandHandler(
            accountRepository,
            transactionalLogRepository
    );

    @Test
    public void shouldThrowAnExceptionWhenAccountOriginNotFound() {
        var accountNumberOrigin = "12345";

        when(accountRepository.findByAccountNumber(accountNumberOrigin)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> commandHandler.handle(
                        new ProcessPaymentOperationCommand(
                                "001",
                                "002",
                                new BigDecimal(10),
                                "user"
                        )
                )
        );
    }

    @Test
    public void shouldThrowAnExceptionWhenAccountDestinationNotFound() {

        var accountNumberDestination = "12345";

        when(accountRepository.findByAccountNumber(accountNumberDestination)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> commandHandler.handle(
                        new ProcessPaymentOperationCommand(
                                "001",
                                "002",
                                new BigDecimal(10),
                                "user"
                        )
                )
        );
    }

    @Test
    public void shouldSaveInsufficientFundsWhenAccountOriginHasNotEnoughFunds() {

        var accountOrigin = "1234";
        var accountDestination = "5678";
        var amount = new BigDecimal(100);
        var username = "operator";

        when(accountRepository.findByAccountNumber(accountOrigin)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountOrigin,
                new BigDecimal(50)
        )));

        when(accountRepository.findByAccountNumber(accountDestination)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountDestination,
                new BigDecimal(100)
        )));

        when(transactionalLogRepository.save(any())).thenReturn(new TransactionalLog(
                new Account(
                        1L,
                        "001",
                        accountOrigin,
                        new BigDecimal(50)
                ),
                new Account(
                        1L,
                        "001",
                        accountDestination,
                        new BigDecimal(100)
                ), new BigDecimal(50),
                new BigDecimal(100),
                BigDecimal.ZERO,
                username,
                TransactionStatus.INSUFFICIENT_FUNDS
        ));

        var response = commandHandler.handle(new ProcessPaymentOperationCommand(
                accountOrigin,
                accountDestination,
                amount,
                username
        ));

        assertEquals(new BigDecimal(50), response.accountOrigin().balance());
        assertEquals(new BigDecimal(100), response.accountDestination().balance());
        assertEquals(TransactionStatus.INSUFFICIENT_FUNDS, response.status());

        verify(transactionalLogRepository, times(1)).save(any());
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void shouldSaveSuccessWhenAccountOriginHasEnoughFunds() {

        var accountOrigin = "1234";
        var balanceAccountOrigin = new BigDecimal(200);
        var accountDestination = "5678";
        var balanceAccountDestination = new BigDecimal(100);
        var amount = new BigDecimal(100);
        var debit = balanceAccountOrigin.subtract(amount);
        var credit = balanceAccountDestination.add(amount);
        var username = "operator";

        when(accountRepository.findByAccountNumber(accountOrigin)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountOrigin,
                balanceAccountOrigin
        )));

        when(accountRepository.findByAccountNumber(accountDestination)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountDestination,
                balanceAccountDestination
        )));

        when(transactionalLogRepository.save(any())).thenReturn(new TransactionalLog(
                new Account(
                        1L,
                        "001",
                        accountOrigin,
                        debit
                ),
                new Account(
                        1L,
                        "001",
                        accountDestination,
                        credit
                ), debit,
                credit,
                amount,
                username,
                TransactionStatus.SUCCESS
        ));

        var response = commandHandler.handle(new ProcessPaymentOperationCommand(
                accountOrigin,
                accountDestination,
                amount,
                username
        ));

        assertEquals(debit, response.accountOrigin().balance());
        assertEquals(credit, response.accountDestination().balance());
        assertEquals(TransactionStatus.SUCCESS, response.status());

        verify(transactionalLogRepository, times(1)).save(any());
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    public void shouldSaveFailureWhenThrownAnException() {

        var accountOrigin = "1234";
        var balanceAccountOrigin = new BigDecimal(200);
        var accountDestination = "5678";
        var balanceAccountDestination = new BigDecimal(100);

        when(accountRepository.findByAccountNumber(accountOrigin)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountOrigin,
                balanceAccountOrigin
        )));

        when(accountRepository.findByAccountNumber(accountDestination)).thenReturn(Optional.of(new Account(
                1L,
                "001",
                accountDestination,
                balanceAccountDestination
        )));

        when(accountRepository.save(any())).thenThrow(new RuntimeException());
        when(transactionalLogRepository.save(any())).thenReturn(new TransactionalLog(
                new Account(
                        1L,
                        "001",
                        "1234",
                        new BigDecimal(10)
                ),
                new Account(
                        1L,
                        "001",
                        "4567",
                        new BigDecimal(20)
                ), new BigDecimal(10),
                new BigDecimal(20),
                new BigDecimal(5),
                "test",
                TransactionStatus.FAIL
        ));

        var response = commandHandler.handle(new ProcessPaymentOperationCommand(
                accountOrigin,
                accountDestination,
                new BigDecimal(5),
                "test"
        ));

        assertEquals(TransactionStatus.FAIL, response.status());
    }


}
