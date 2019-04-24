package moneytransfer.controllers;

import moneytransfer.dao.AccountDAO;
import moneytransfer.dao.DAOFactory;
import moneytransfer.dao.TransactionDAO;
import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.exceptions.InsufficientFundsException;
import moneytransfer.exceptions.TransactionDoesNotExistException;
import moneytransfer.exceptions.UnexpectedAccountUpdateException;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;
import moneytransfer.requests.TransactionRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsController {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionsController() {
        accountDAO = DAOFactory.getFactory().getAccountDAO();
        transactionDAO = DAOFactory.getFactory().getTransactionDAO();
    }

    public TransactionsController(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    @GET
    public List<Transaction> listTransactions() {
        return transactionDAO.listTransactions();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Transaction createTransaction(TransactionRequest transactionRequest)
            throws AccountDoesNotExistException, InsufficientFundsException, UnexpectedAccountUpdateException {
        List<Account> accounts = accountDAO.transferBalance(
                transactionRequest.getSourceAccountId(),
                transactionRequest.getTargetAccontId(),
                transactionRequest.getAmount());

        if (accounts.size() != 2)
            throw new UnexpectedAccountUpdateException(transactionRequest.getSourceAccountId(), transactionRequest.getTargetAccontId());

        return transactionDAO.create(new Account(accounts.get(0)), new Account(accounts.get(1)), transactionRequest.getAmount());
    }

    @GET
    @Path("/{id}")
    public Transaction getTransaction(@PathParam("id") String id) throws TransactionDoesNotExistException {
        return transactionDAO.get(id);
    }
}
