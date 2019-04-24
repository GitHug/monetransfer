package moneytransfer.exceptions;

import javax.ws.rs.core.Response;

public class AccountDoesNotExistException extends MoneyTransferException {

    public AccountDoesNotExistException(String accountId) {
        super(String.format("Account with id %s does not exist.", accountId));
    }

    @Override
    public Response.Status errorResponseStatus() {
        return Response.Status.NOT_FOUND;
    }
}
