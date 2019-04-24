package moneytransfer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountDoesNotExistExceptionTest {

    @Test
    void verify_exception_message() {
        String accountId = "account_id";

        AccountDoesNotExistException exception = new AccountDoesNotExistException(accountId);

        assertEquals("Account with id " + accountId + " does not exist.", exception.getMessage());
    }
}