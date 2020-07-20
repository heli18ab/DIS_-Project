package server.repository;

import Cache.AccountCache;
import server.controllers.AccountController;
import server.model.Account;
import server.model.Transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A repository class for database operations for account objects.
 */
public class AccountRepository extends BaseRepository {

    private Connection connection;
    private static BaseRepository dbCon;


    /**
     * Get account
     * @param account_id The id of the customer.
     * @return account
     */
    public Account getAccount(int account_id) {
        Account account = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM accounts WHERE account_id = ?";
            ps = super.prepareQuery(sql);

            ps.setInt(1, account_id);
            rs = super.executePreparedStatementQuery(ps);

            if (rs != null && rs.next())

                account = new Account(rs.getInt("account_id"), rs.getInt("customer_id"), rs.getDouble("balance"), rs.getDate("created_date"), rs.getDate("updated_date"));



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }


        return account;
    }


/**
 * Get all accounts
 * @return accounts
 */
public ArrayList<Account> getAllAccounts() throws SQLException {

          if (connection == null) {
            connection = getConnection();
        }

        //Merk denne metoden
        String sql = "SELECT * FROM  accounts";

        PreparedStatement ps = super.prepareQuery( sql, connection);

        ResultSet rs = super.executePreparedStatementQuery(ps);
        ArrayList<Account> accounts = new ArrayList<>();

        if (rs == null) {
            return accounts;
        }
        try {
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getInt("customer_id"), rs.getDouble("balance"), rs.getDate("created_date"), rs.getDate("updated_date"));

                accounts.add(account);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }


        return accounts;
    }


    /**
     * Get accounts for a customer
     * @param customerId The id of the customer.
     * @return account
     */
    public ArrayList<Account> getAccountsForCustomer(int customerId) throws SQLException {

        if (connection == null) {
            connection = getConnection();
        }

        String sql = "SELECT * FROM  accounts WHERE customer_id = ?";

        PreparedStatement ps = super.prepareQuery(sql, connection);

        try {
            ps.setInt(1, customerId);
        } catch (SQLException e) {

        }
        ResultSet rs = super.executePreparedStatementQuery(ps);
        ArrayList<Account> accounts = new ArrayList<>();

        if (rs == null) {
            return accounts;
        }
        try {
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("balance"),
                        rs.getDate("created_date"),
                        rs.getDate("updated_date"));


                account.getUpdated_date().toString();

                System.out.println("asdas" + account.getUpdated_date().toString());
                //addInterest();
                accounts.add(account);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }


        return accounts;
    }

    /**
     * Create account
     * @param account the new account object
     * @return account
     */
    public Account createAccount(Account account) throws SQLException {

        if (connection == null) {
            connection = getConnection();
        }


        String sql = "INSERT INTO accounts(customer_id, balance) VALUES (?, ?)";

        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);

            ps = prepareInsert(sql, connection);
            ps.setInt(1, account.getCustomer_id());
            ps.setDouble(2, account.getBalance());
            int primary_key = executeInsertPreparedStatement(ps);
            account.setAccount_id(primary_key);

            connection.commit();
           connection.setAutoCommit(true);


        } catch (SQLException ex) {
            try {
             connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } finally {
            cleanUp(ps);
        }

        AccountController.accountCache.getAccounts(true);


        return account;
    }


    /**
     * withdraw and deposit
     * @param account that will be edited
     */
    public void withDrawAndDeposit(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance =  balance - ? WHERE account_id = ?;";

        if (connection == null) {
            connection = getConnection();
        }

        PreparedStatement ps = null;
        try {
          connection.setAutoCommit(false);

            ps = prepareQuery( sql, connection);
            ps.setDouble(1, account.getBalance());
            ps.setInt(2, account.getAccount_id());
            executePreparedStatementQuery(ps);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } finally {
            cleanUp(ps);
        }

        AccountController.accountCache.getAccounts(true);
    }


    /**
     * Transfer method
     * @param transfer a transfer object
     */
    public void transfer(Transfer transfer) throws SQLException {
        if (connection == null) {
            connection = getConnection();
        }


        String sql = "UPDATE accounts SET balance =  balance - ? WHERE account_id = ?;";
        String sql1 = "UPDATE accounts SET balance =  balance + ? WHERE account_id = ?;";


        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        try {

            this.connection.setAutoCommit(false);
            ps = prepareQuery( sql, connection);
            ps1 = prepareQuery( sql1, connection);

            ps.setInt(1, transfer.getAmount());
            ps.setInt(2, transfer.getAccount_from());
            executePreparedStatementQuery(ps);


            ps1.setInt(1, transfer.getAmount());
            ps1.setInt(2, transfer.getAccount_to());
            executePreparedStatementQuery(ps1);

            connection.commit();
            connection.setAutoCommit(true);


        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cleanUp(ps);
                cleanUp(ps1);
            }

        }
        AccountController.accountCache.getAccounts(true);


    }


    /**
     * Make intrest
     * @param account
     */
    public void makeInterest(Account account) throws SQLException {


        long amountOfTime = (System.currentTimeMillis()-account.getUpdated_date().getTime());
        long days = TimeUnit.MILLISECONDS.toDays(amountOfTime);
        if(days>0) {
            double balance = account.getBalance();
            double rate;
            rate = 0.008;
            double balanceWithInterestRate = balance * Math.pow(1 + rate, days);
            account.setBalance(balanceWithInterestRate);
        }
        String sql = "UPDATE accounts SET balance = ?, updated_date=CURRENT_TIMESTAMP WHERE account_id = ?";
        PreparedStatement ps = prepareQuery(sql);
        try{
            ps.setDouble(1,account.getBalance());
            ps.setInt(2,account.getAccount_id());
            executePreparedStatementQuery(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            cleanUp(ps);
        }
        AccountController.accountCache.getAccounts(true);

    }



}




