package moneytransfer.exceptions;

import javax.ws.rs.core.Response;

public abstract class MoneyTransferException extends Exception {

    public MoneyTransferException(String message) {
        super(message);
    }

    public abstract Response.Status errorResponseStatus();
}
