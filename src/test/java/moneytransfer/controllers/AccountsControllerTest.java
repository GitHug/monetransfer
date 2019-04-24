package moneytransfer.controllers;

import moneytransfer.dao.AccountDAO;
import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.models.Account;
import moneytransfer.requests.AccountRequest;
import moneytransfer.requests.UpdateAccountRequest;
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
class AccountsControllerTest {

    @Mock
    AccountDAO accountDAOMock;

    @InjectMocks
    AccountsController instance;

    String failId = "fail";


    Account expectedAccount = new Account("Test account");
    Account expectedAccount2 = new Account("Test account 2");

    @BeforeEach
    void setUp() throws AccountDoesNotExistException {
        lenient().when(accountDAOMock.get(anyString())).thenAnswer(invocationOnMock -> {
            if (failId.equals(invocationOnMock.getArgument(0))) throw new AccountDoesNotExistException(failId);
            return expectedAccount;
        });

        lenient().when(accountDAOMock.create(anyString())).thenReturn(expectedAccount);
        lenient().when(accountDAOMock.listAccounts()).thenReturn(List.of(expectedAccount, expectedAccount2));

        lenient().when(accountDAOMock.delete(anyString())).thenAnswer(invocationOnMock -> {
            if (failId.equals(invocationOnMock.getArgument(0))) throw new AccountDoesNotExistException(failId);
            return expectedAccount;
        });

        lenient().when(accountDAOMock.update(anyString(), anyString(), any(BigDecimal.class)))
                .thenAnswer(invocationOnMock -> {
            if (failId.equals(invocationOnMock.getArgument(0))) throw new AccountDoesNotExistException(failId);
            return expectedAccount;
        });
    }

    @Test
    void listAccounts_should_return_list_of_accounts() {
        // when
        List<Account> accounts = instance.listAccounts();

        // then
        assertEquals(List.of(expectedAccount, expectedAccount2), accounts);
    }

    @Test
    void getAccount_should_return_an_account_if_account_with_id_exists() throws Exception {
        // when
        Account account = instance.getAccount("account_id");

        // then
        assertEquals(expectedAccount, account);
    }

    @Test
    void getAccount_should_throw_exception_dao_throws_exception() {
        assertThrows(AccountDoesNotExistException.class, () -> instance.getAccount(failId));
    }

    @Test
    void createAccount_should_return_the_created_account() {
        // given
        AccountRequest request = new AccountRequest();
        request.setName("test");

        // when
        Account account = instance.createAccount(request);

        // then
        assertEquals(expectedAccount, account);
    }

    @Test
    void deleteAccount_should_return_the_deleted_account() throws Exception {
        // when
        Account account = instance.deleteAccount("account_id");

        // then
        assertEquals(expectedAccount, account);
    }

    @Test
    void deleteAccount_should_throw_exception_if_dao_throws_exception() {
        assertThrows(AccountDoesNotExistException.class, () -> instance.deleteAccount(failId));
    }

    @Test
    void updateAccount_should_return_the_updated_account() throws Exception {
        // given
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setName("test");
        request.setBalance(BigDecimal.ONE);

        // when
        Account account = instance.updateAccount("account_id", request);

        // then
        assertEquals(expectedAccount, account);
    }

    @Test
    void updateAccount_should_throw_exception_if_dao_throws_exception() {
        // given
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setName("test");
        request.setBalance(BigDecimal.ONE);

        assertThrows(AccountDoesNotExistException.class, () ->
                instance.updateAccount(failId, request));
    }
}