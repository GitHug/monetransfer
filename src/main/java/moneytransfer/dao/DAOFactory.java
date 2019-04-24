package moneytransfer.dao;

public class DAOFactory {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private static DAOFactory instance;

    private DAOFactory () {
        accountDAO = new AccountDAOImpl();
        transactionDAO = new TransactionDAOImpl();
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    public static DAOFactory getFactory () {
        if (instance == null) instance = new DAOFactory();
        return instance;
    }
}
