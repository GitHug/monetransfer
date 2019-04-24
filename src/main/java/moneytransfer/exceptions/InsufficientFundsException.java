package moneytransfer.exceptions;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class InsufficientFundsException extends MoneyTransferException {

    public InsufficientFundsException(String accountId, BigDecimal amount) {
        super(String.format("Account with id %s has insufficient funds for %.2f.", accountId, amount));
    }

    @Override
    public Response.Status errorResponseStatus() {
        return Response.Status.CONFLICT;
    }
}
