package moneytransfer.exceptions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsufficientFundsExceptionTest {

    @Test
    void verify_exception_message() {
        String accountId = "account_id";
        BigDecimal amount = BigDecimal.valueOf(1337.42);
        String amountString = String.format("%.2f", amount);


        InsufficientFundsException exception = new InsufficientFundsException(accountId, amount);

        assertEquals("Account with id " + accountId + " has insufficient funds for " + amountString + ".", exception.getMessage());
    }

}