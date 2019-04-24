package moneytransfer.exceptions;

import javax.ws.rs.core.Response;

public class TransactionDoesNotExistException extends MoneyTransferException {

    public TransactionDoesNotExistException(String transactionId) {
        super(String.format("Transaction with id %s does not exist.", transactionId));
    }

    @Override
    public Response.Status errorResponseStatus() {
        return Response.Status.NOT_FOUND;
    }
}
