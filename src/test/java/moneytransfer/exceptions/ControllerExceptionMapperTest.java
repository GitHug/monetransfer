package moneytransfer.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ControllerExceptionMapperTest {

    @InjectMocks
    ControllerExceptionMapper instance;

    @Test
    void toResponse_verify_account_does_not_exist_exception_message_and_code() {
        // given
        MoneyTransferException exception = new AccountDoesNotExistException("account_id");

        // when
        Response response = instance.toResponse(exception);

        // then
        assertEquals(new ErrorResponseEntity(exception.getMessage(), 404), response.getEntity());
    }

    @Test
    void toResponse_verify_insufficient_funds_exception_message_and_code() {
        // given
        MoneyTransferException exception = new InsufficientFundsException("account_id", BigDecimal.TEN);

        // when
        Response response = instance.toResponse(exception);

        // then
        assertEquals(new ErrorResponseEntity(exception.getMessage(), 409), response.getEntity());
    }

    @Test
    void toResponse_verify_transaction_does_not_exist_exception_message_and_code() {
        // given
        MoneyTransferException exception = new TransactionDoesNotExistException("transaction_id");

        // when
        Response response = instance.toResponse(exception);

        // then
        assertEquals(new ErrorResponseEntity(exception.getMessage(), 404), response.getEntity());
    }

    @Test
    void toResponse_verify_unexpected_account_update_exception_message_and_code() {
        // given
        MoneyTransferException exception = new UnexpectedAccountUpdateException("transaction_id", "transaction_id");

        // when
        Response response = instance.toResponse(exception);

        // then
        assertEquals(new ErrorResponseEntity(exception.getMessage(), 500), response.getEntity());
    }
}