package moneytransfer.dao;

import moneytransfer.exceptions.AccountDoesNotExistException;
import moneytransfer.exceptions.InsufficientFundsException;
import moneytransfer.models.Account;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AccountDAOImpl implements AccountDAO {
    private final ConcurrentMap<String, Account> accounts;

    public AccountDAOImpl () {
        accounts = new ConcurrentHashMap<>();
    }

    public Account create(String name) {
        Account account = new Account(name);

        if (accounts.putIfAbsent(account.getId(), account) != null) {
            return null;
        }

        return account;
    }

    public Account get(String id) throws AccountDoesNotExistException {
        if (accounts.containsKey(id)) return accounts.get(id);
        throw new AccountDoesNotExistException(id);
    }

    public Account delete(String id) throws AccountDoesNotExistException {
        if (accounts.containsKey(id)) {
         accounts.get(id).setActive(false);
         return accounts.get(id);
        }
        throw new AccountDoesNotExistException(id);
    }

    public Account update(String id, String name, BigDecimal balance) throws AccountDoesNotExistException {
        if (accounts.containsKey(id)) {

            if (name != null) {
                accounts.get(id).setName(name);
            }

            if (balance != null) {
                accounts.get(id).setBalance(balance);
            }

            return accounts.get(id);
        }

        throw new AccountDoesNotExistException(id);
    }

    public List<Account> transferBalance(String sourceAccountId, String targetAccountId, BigDecimal amount) throws AccountDoesNotExistException, InsufficientFundsException {
        Account source = get(sourceAccountId);
        Account target = get(targetAccountId);

        synchronized (source) {
            if(source.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(sourceAccountId, amount);
            }

            source.setBalance(source.getBalance().subtract(amount));
        }
        target.setBalance(target.getBalance().add(amount));

        return List.of(source, target);
    }

    public List<Account> listAccounts () {
        return accounts
                .values()
                .stream()
                .filter(Account::isActive)
                .sorted(Comparator.comparing((Account account) -> account.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
