package moneytransfer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDoesNotExistExceptionTest {

    @Test
    void verify_exception_message() {
        String transactionId = "transaction_id";

        TransactionDoesNotExistException exception = new TransactionDoesNotExistException(transactionId);

        assertEquals("Transaction with id " + transactionId + " does not exist.", exception.getMessage());
    }

}