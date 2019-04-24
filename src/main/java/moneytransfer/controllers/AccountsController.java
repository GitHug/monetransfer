package moneytransfer.controllers;

import moneytransfer.dao.AccountDAO;
import moneytransfer.dao.DAOFactory;
import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.models.Account;
import moneytransfer.requests.AccountRequest;
import moneytransfer.requests.UpdateAccountRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountsController {
    private final AccountDAO accountDAO;

    public AccountsController() {
        accountDAO = DAOFactory.getFactory().getAccountDAO();
    }

    public AccountsController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GET
    public List<Account> listAccounts() {
        return accountDAO.listAccounts();
    }

    @GET
    @Path("/{id}")
    public Account getAccount(@PathParam("id") String id) throws AccountDoesNotExistException {
        return accountDAO.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Account createAccount(AccountRequest accountRequest) {
        return accountDAO.create(accountRequest.getName());
    }

    @DELETE
    @Path("/{id}")
    public Account deleteAccount(@PathParam("id") String id) throws AccountDoesNotExistException {
        return accountDAO.delete(id);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account updateAccount(@PathParam("id") String id, UpdateAccountRequest accountRequest) throws AccountDoesNotExistException {
        return accountDAO.update(id, accountRequest.getName(), accountRequest.getBalance());
    }
}
