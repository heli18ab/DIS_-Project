package server.model;

import java.util.Date;

/**
 * A class representing an account in the system.
 *
 */
public class Account {

    private int account_id;
    private int customer_id; //fra customer;
    private double balance;
    private Date created_date;
    private Date updated_date;


    /**
     * Constructor that sets all of the variabels for an account.
     *@param account_id
     * @param customerId
     * @param balance
     * @param updated_date
     * @param created_date
     */


    public Account(int account_id, int customerId, double balance,Date created_date, Date updated_date) {
        this.account_id = account_id;
        this.customer_id =customerId;
        this.balance = balance;
        this.created_date = created_date;
        this.updated_date = updated_date;

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

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Date updated_date) {
        this.updated_date = updated_date;
    }

    //TODO: Implement the class. See init.sql for suggested fields - though remember that they may be
    //named differently here, if you so wish.
}
