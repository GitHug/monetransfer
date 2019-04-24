package moneytransfer.dao;

import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.exceptions.InsufficientFundsException;
import moneytransfer.models.Account;

import java.math.BigDecimal;
import java.util.List;


public interface AccountDAO {
    Account create(String name);
    Account get(String id) throws AccountDoesNotExistException;
    Account delete(String id) throws AccountDoesNotExistException;
    Account update(String id, String name, BigDecimal balance) throws AccountDoesNotExistException;
    List<Account> transferBalance(String sourceAccountId, String targetAccountId, BigDecimal balance)
            throws AccountDoesNotExistException, InsufficientFundsException;
    List<Account> listAccounts ();
}
