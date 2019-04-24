package moneytransfer.dao;

import moneytransfer.exceptions.TransactionDoesNotExistException;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransactionDAOImplTest {

    @InjectMocks
    TransactionDAOImpl instance;

    @Mock
    Account source;

    @Mock
    Account target;

    @Test
    void create_should_create_a_transaction() {
        // when
        Transaction transaction = instance.create(source, target, BigDecimal.TEN);

        // then
        assertEquals(source, transaction.getSourceAccount());
        assertEquals(target, transaction.getTargetAccount());
        assertEquals(BigDecimal.TEN, transaction.getAmount());
    }

    @Test
    void listTransactions_should_list_all_stored_transactions() {
        // given
        Transaction transaction1 = instance.create(source, target, BigDecimal.ZERO);
        Transaction transaction2 = instance.create(source, target, BigDecimal.ONE);
        Transaction transaction3 = instance.create(source, target, BigDecimal.TEN);

        // when
        List<Transaction> transactions = instance.listTransactions();

        // then
        assertEquals(List.of(transaction1, transaction2, transaction3), transactions);
    }

    @Test
    void get_should_get_a_transaction_by_id() throws Exception {
        // given
        Transaction expectedTransaction = instance.create(source, target, BigDecimal.ZERO);

        // when
        Transaction transaction = instance.get(expectedTransaction.getId());

        // then
        assertEquals(expectedTransaction, transaction);
    }

    @Test
    void get_should_throw_exception_if_no_account_with_id_exists() {
        // given
        instance.create(source, target, BigDecimal.ZERO);

        // then
        assertThrows(TransactionDoesNotExistException.class, () -> instance.get("test"));
    }
}