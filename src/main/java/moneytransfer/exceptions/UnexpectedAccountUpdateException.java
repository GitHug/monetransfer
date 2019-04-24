package moneytransfer.exceptions;

import javax.ws.rs.core.Response;

public class UnexpectedAccountUpdateException extends MoneyTransferException {

    public UnexpectedAccountUpdateException(String sourceAccountId, String targetAccountId) {
        super(String.format("An unexpected account update exception has occurred when transferring money from " +
            "source account id %s to target account id %s.", sourceAccountId, targetAccountId));
    }

    @Override
    public Response.Status errorResponseStatus() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
