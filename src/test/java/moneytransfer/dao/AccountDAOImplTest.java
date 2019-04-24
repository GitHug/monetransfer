package moneytransfer.dao;

import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.exceptions.InsufficientFundsException;
import moneytransfer.models.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountDAOImplTest {

    @InjectMocks
    AccountDAOImpl instance;

    @Test
    void create_should_create_an_account_with_the_given_name() {
        // given
        String expectedName = "name";

        // when
        Account account = instance.create(expectedName);

        assertNotNull(account);
        assertEquals(expectedName, account.getName());
    }

    @Test
    void get_should_get_an_account_by_id() throws Exception {
        // given
        Account expectedAccount = instance.create("name");

        // when
        Account account = instance.get(expectedAccount.getId());

        // then
        assertEquals(expectedAccount, account);
    }

    @Test
    void get_should_throw_exception_if_no_account_with_id_exists() {
        // given
        instance.create("name");

        // then
        assertThrows(AccountDoesNotExistException.class, () -> instance.get("test"));
    }

    @Test
    void delete_should_mark_account_as_inactive() throws Exception {
        // given
        Account account = instance.create("name");
        assertTrue(account.isActive());

        // when
        instance.delete(account.getId());

        // then
        assertFalse(account.isActive());
    }

    @Test
    void delete_should_throw_exception_if_no_account_with_id_exists() {
        // given
        instance.create("name");

        // when
        assertThrows(AccountDoesNotExistException.class, () -> instance.delete("test"));
    }

    @Test
    void update_should_update_name_and_account_balance() throws Exception {
        // given
        Account account = instance.create("name");
        assertEquals("name", account.getName());
        assertEquals(BigDecimal.ZERO, account.getBalance());

        // when
        instance.update(account.getId(), "test", BigDecimal.ONE);

        // then
        assertEquals("test", account.getName());
        assertEquals(BigDecimal.ONE, account.getBalance());
    }

    @Test
    void update_should_not_update_name_if_new_name_is_null() throws Exception {
        // given
        Account account = instance.create("name");

        // when
        instance.update(account.getId(), null, BigDecimal.ONE);

        // then
        assertEquals("name", account.getName());
    }

    @Test
    void update_should_not_update_balance_if_new_balance_is_null() throws Exception {
        // given
        Account account = instance.create("name");

        // when
        instance.update(account.getId(), "test", null);

        // then
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void update_should_throw_exception_if_no_account_with_id_exists() {
        // given
        instance.create("name");

        // then
        assertThrows(AccountDoesNotExistException.class, () -> instance.update("test", "test", BigDecimal.ONE));
    }


    @Test
    void transferBalance_should_transfer_amount_from_source_to_target_account() throws Exception {
        // given
        Account source = instance.create("source");
        Account target = instance.create("target");

        instance.update(source.getId(), null, BigDecimal.valueOf(1000));
        instance.update(target.getId(), null, BigDecimal.valueOf(937));

        // when
        List<Account> updatedAccounts = instance.transferBalance(source.getId(), target.getId(), BigDecimal.valueOf(400));

        // then
        assertEquals(BigDecimal.valueOf(600), source.getBalance());
        assertEquals(BigDecimal.valueOf(1337), target.getBalance());

        assertEquals(List.of(source, target), updatedAccounts);
    }

    @Test
    void transferBalance_should_throw_exception_if_source_account_balance_is_less_than_amount() throws Exception {
        // given
        Account source = instance.create("source");
        Account target = instance.create("target");

        instance.update(source.getId(), null, BigDecimal.ONE);

        // then
        assertThrows(InsufficientFundsException.class, () -> instance.transferBalance(source.getId(), target.getId(), BigDecimal.TEN));
    }

    @Test
    void transferBalance_should_not_throw_exception_if_source_account_balance_is_equal_to_amount() throws Exception {
        // given
        Account source = instance.create("source");
        Account target = instance.create("target");

        instance.update(source.getId(), null, BigDecimal.TEN);

        // when
        instance.transferBalance(source.getId(), target.getId(), BigDecimal.TEN);

        // then
        assertEquals(BigDecimal.ZERO, source.getBalance());
        assertEquals(BigDecimal.TEN, target.getBalance());
    }

    @Test
    void transferBalance_should_throw_exception_if_source_account_id_does_not_exist() {
        // given
        instance.create("source");
        Account target = instance.create("target");

        // then
        assertThrows(AccountDoesNotExistException.class, () -> instance.transferBalance("test", target.getId(), BigDecimal.TEN));
    }

    @Test
    void transferBalance_should_throw_exception_if_target_account_id_does_not_exist() {
        // given
        Account source = instance.create("source");
        instance.create("target");

        // then
        assertThrows(AccountDoesNotExistException.class, () -> instance.transferBalance(source.getId(), "test", BigDecimal.TEN));
    }

    @Test
    void listAccounts_should_list_all_active_accounts() {
        // given
        Account account1 = instance.create("1");
        Account account2 = instance.create("2");
        Account account3 = instance.create("3");

        // when
        List<Account> accounts = instance.listAccounts();

        // then
        assertEquals(List.of(account1, account2, account3), accounts);
    }

    @Test
    void listAccounts_should_not_list_inactive_accounts() throws Exception {
        // given
        Account account1 = instance.create("1");
        Account account2 = instance.create("2");
        Account account3 = instance.create("3");

        instance.delete(account2.getId());

        // when
        List<Account> accounts = instance.listAccounts();

        // then
        assertEquals(List.of(account1, account3), accounts);
    }
}