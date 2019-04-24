package moneytransfer.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnexpectedAccountUpdateExceptionTest {

    @Test
    void verify_exception_message() {
        String sourceAccountId = "source_id";
        String targetAccountId = "target_id";

        UnexpectedAccountUpdateException exception = new UnexpectedAccountUpdateException(sourceAccountId, targetAccountId);

        assertEquals("An unexpected account update exception has occurred when transferring money from " +
                "source account id " + sourceAccountId + " to target account id " + targetAccountId + ".", exception.getMessage());
    }

}