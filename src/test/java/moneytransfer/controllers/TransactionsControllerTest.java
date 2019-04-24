package moneytransfer.controllers;

import moneytransfer.dao.AccountDAO;
import moneytransfer.dao.TransactionDAO;
import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.exceptions.InsufficientFundsException;
import moneytransfer.exceptions.TransactionDoesNotExistException;
import moneytransfer.exceptions.UnexpectedAccountUpdateException;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;
import moneytransfer.requests.TransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class TransactionsControllerTest {

    @Mock
    AccountDAO accountDAOMock;

    @Mock
    TransactionDAO transactionDAOMock;

    @InjectMocks TransactionsController instance;

    @Mock
    Transaction transaction1, transaction2;

    @Mock
    Account account1, account2;

    String accountDoesNotExistId = "account_does_not_exist_id";
    String insuffientFundsAccountId = "insufficient_funds_account_id";
    String unexpectedAccountUpdateId = "unexpected_account_update_id";
    String transactionDoesNotExistId = "transaction_does_not_exist_id";

    @BeforeEach
    void setUp() throws AccountDoesNotExistException, InsufficientFundsException, TransactionDoesNotExistException {
        lenient().when(transactionDAOMock.listTransactions()).thenReturn(List.of(transaction1, transaction2));

        lenient().when(accountDAOMock.transferBalance(anyString(), anyString(), any(BigDecimal.class)))
                .thenAnswer(invocationOnMock -> {
                   if (accountDoesNotExistId.equals(invocationOnMock.getArgument(0)))
                       throw new AccountDoesNotExistException(invocationOnMock.getArgument(0));

                   if (insuffientFundsAccountId.equals(invocationOnMock.getArgument(0)))
                       throw new InsufficientFundsException(invocationOnMock.getArgument(0), invocationOnMock.getArgument(2));

                   if (unexpectedAccountUpdateId.equals(invocationOnMock.getArgument(0)))
                       return List.of(account1);

                   return List.of(account1, account2);
                });

        lenient().when(transactionDAOMock.create(any(Account.class), any(Account.class), any(BigDecimal.class)))
                .thenReturn(transaction1);

        lenient().when(transactionDAOMock.get(anyString())).thenAnswer(invocationOnMock -> {
            if (transactionDoesNotExistId.equals(invocationOnMock.getArgument(0)))
                throw new TransactionDoesNotExistException(invocationOnMock.getArgument(0));
            return transaction1;
        });
    }

    @Test
    void listTransaction_should_list_all_transactions() {
        // when
        List<Transaction> transactions = instance.listTransactions();

        // then
        assertEquals(List.of(transaction1, transaction2), transactions);
    }

    @Test
    void createTransaction_should_transfer_balance_and_create_transaction()
            throws InsufficientFundsException, UnexpectedAccountUpdateException, AccountDoesNotExistException {
        // given
        TransactionRequest request = createRequest("id");

        // when
        Transaction transaction = instance.createTransaction(request);

        // then
        assertEquals(transaction1, transaction);
    }

    @Test
    void createTransaction_should_throw_account_not_found_exception() {
        // given
        TransactionRequest request = createRequest(accountDoesNotExistId);

        // then
        assertThrows(AccountDoesNotExistException.class, () -> instance.createTransaction(request));
    }

    @Test
    void createTransaction_should_throw_insufficient_funds_exception() {
        // given
        TransactionRequest request = createRequest(insuffientFundsAccountId);

        // then
        assertThrows(InsufficientFundsException.class, () -> instance.createTransaction(request));
    }

    @Test
    void createTransaction_should_throw_unexpected_account_update_exception() {
        // given
        TransactionRequest request = createRequest(unexpectedAccountUpdateId);

        // then
        assertThrows(UnexpectedAccountUpdateException.class, () -> instance.createTransaction(request));
    }

    @Test
    void getTransaction_should_return_a_transaction_if_transaction_with_id_exists() throws TransactionDoesNotExistException {
        // when
        Transaction transaction = instance.getTransaction("transaction_id");

        // then
        assertEquals(transaction1, transaction);
    }

    @Test
    void getTransaction_should_throw_exception() {
        assertThrows(TransactionDoesNotExistException.class, () -> instance.getTransaction(transactionDoesNotExistId));
    }

    private TransactionRequest createRequest(String sourceAccountId) {
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccontId("id2");
        request.setAmount(BigDecimal.ONE);

        return request;
    }
}