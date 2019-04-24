package moneytransfer.dao;

import moneytransfer.exceptions.TransactionDoesNotExistException;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDAO {

    Transaction create(Account source, Account target, BigDecimal amount);

    List<Transaction> listTransactions();

    Transaction get(String id) throws TransactionDoesNotExistException;
}
