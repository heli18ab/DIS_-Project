package server.model;

import server.util.Hashing;

/**
 * Represents a customer in the system.
 */
public class Customer {
    private String name;
    private String username;
    private String password;
    private int customerId;
    private int customerType;

    private Hashing hashing;

    /**
     * Constructor that sets all of the variabels for a customer.
     *@param name
     * @param username
     * @param password
     * @param customerType
     */
    public Customer(String name, String username, String password, int customerId, int customerType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.customerId = customerId;
        this.customerType = customerType;
    }

    public Customer(){}

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password = new Hashing().hashWithSalt(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

}
