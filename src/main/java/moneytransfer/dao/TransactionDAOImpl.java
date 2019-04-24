package moneytransfer.dao;

import moneytransfer.exceptions.TransactionDoesNotExistException;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class TransactionDAOImpl implements TransactionDAO {
    private final ConcurrentMap<String, Transaction> transactions;

    public TransactionDAOImpl () {
        transactions = new ConcurrentHashMap<>();
    }

    @Override
    public Transaction create(Account source, Account target, BigDecimal amount) {
        Transaction transaction = new Transaction(source, target, amount);

        if (transactions.putIfAbsent(transaction.getId(), transaction) != null) {
            return null;
        }

        return transaction;
    }


    @Override
    public List<Transaction> listTransactions() {
        return transactions
                .values()
                .stream()
                .sorted(Comparator.comparing((Transaction transaction) -> transaction.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public Transaction get(String id) throws TransactionDoesNotExistException {
        if (transactions.containsKey(id)) return transactions.get(id);
        throw new TransactionDoesNotExistException(id);
    }
}
