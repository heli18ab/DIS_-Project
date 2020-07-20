package model;


/**
 * Represents a transfer in the system.
 */
public class Transfer {

    private int account_from;
    private int account_to;
    private int amount;


    /**
     * Constructor that sets all of the variabels for a transfer.
     *@param account_from
     * @param account_to
     * @param amount
     */
    public Transfer(int account_from, int account_to, int amount){
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }


    public  Transfer(){

    }
    public int getAccount_from() {
        return account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

