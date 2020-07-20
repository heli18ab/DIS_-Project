package model;


/**
 * A class representing an account in the system.
 *
 */
public class Account {

    private int account_id;
    private int customer_id; //from customer;
    private double balance;




    /**
     * Constructor that sets all of the variabels for an account.
     *@param account_id
     * @param customerId
     * @param balance
     */

    public Account(int account_id, int customerId, double balance) {

        this.account_id = account_id;
        this.customer_id = customerId;
        this.balance = balance;


    }

    public Account() {
    }

    public double getBalance() {
        return balance;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }


    @Override
    public String toString() {
        return "Account ID: " + getAccount_id() + " Customer ID: " + getCustomer_id() + " Balance: " + getBalance();


    }
}
