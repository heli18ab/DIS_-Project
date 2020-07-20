package controller;

import com.google.gson.Gson;
import cyclops.control.Trampoline;
import model.Customer;
import util.Encryption;
import util.NetworkUtil;
import view.TellerMainView;
import view.CustomerMainView;

import java.io.IOException;
import java.util.Arrays;

/**
 * The controller for customer related operations.
 */
public class CustomerController extends MainController{


    //declaration of classes that will be used later
    private NetworkUtil networkUtil;
    private TellerMainView tellerMainView;
    private Customer customer;;
    private CustomerMainView customerMainView;

    private static final String BASE = "http://localhost:8080/exam/";

    public CustomerController(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    public CustomerController(){

    }
    /**
     * Gets all customers from server.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> getAllCustomers() {
        String url = BASE+"customers";
        try
        {
            String response = networkUtil.httpRequest(url, "GET");

             response = new Gson().fromJson(response, String.class);
             response = Encryption.encryptDecrypt(response);



            Customer[] customers = new Gson().fromJson(response, Customer[].class);

            return Trampoline.more(() -> tellerMainView.ShowAllCustomers(Arrays.asList(customers)));
        }
        catch (IOException e)
        {
            return Trampoline.more(()-> tellerMainView.ShowMessage("Error getting customers from server. Message: " + e.getMessage()));
        }

    }

    /**
     * Gets a customer from the server.
     * @param userId The userid of the customer.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  getCustomer(int userId) {
        try
        {
            String url = BASE+"customers/" + (userId);
            String response = networkUtil.httpRequest(url, "GET");

            response = new Gson().fromJson(response, String.class);
            response = Encryption.encryptDecrypt(response);


            Customer customer = new Gson().fromJson(response, Customer.class);

            return Trampoline.more(() -> tellerMainView.ShowCustomer(customer));
        }
        catch (IOException e)
        {
            return Trampoline.more(() -> tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage()));
        }

    }

    /**
     * Gets a customer from the server - chek if customer do exist..
     * @param customerID The userid of the customer.
     * @param type type defines admin or customer
     * @return customer
     */
    public Customer checkCustomer(int customerID, int type) {
        try {
            String url = BASE+"customers/" + (customerID);
            String response = networkUtil.httpRequest(url, "GET");

            response = new Gson().fromJson(response, String.class);
            response = Encryption.encryptDecrypt(response);

            Customer customer = new Gson().fromJson(response, Customer.class);
            return customer;
        } catch (IOException e) {
            if (type == 2) {
                tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());
            } else
                customerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());
        }
        return null;
    }


    /**
     * Creates a customer on the server.
     * @param name The name of the customer.
     * @param username The username of the customer.
     * @param password The name of the customer.
     * @param type type defines admin or customer
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  createCustomer (String name, String username, String password, int type) {
        String url = BASE+"customers";
        try {
            Customer c = new Customer();
            c.setName(name);
            c.setUsername(username);
            c.setPassword(password);
            c.setCustomerType(type);
            String jsonInputString = new Gson().toJson(c);

            networkUtil.httpRequest(url, "POST",jsonInputString);

        } catch (Exception e) {
            return Trampoline.more(() -> tellerMainView.ShowMessage("Error creating customer. Message: " + e.getMessage()));
        }
        return Trampoline.more(() -> tellerMainView.ShowMessage("Customer created. "));

    }

    /**
     * Updates a username on the server.
     * @param id The id of the customer.
     * @param username The name of the customer.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> updateUserName(int id, String username, int type) {

        String url = BASE + "customers/" + "usernameChange";

        try {

            Customer c = new Customer();
            c.setUsername(username);
            c.setCustomerId(id);

            String jsonInputString = new Gson().toJson(c);

            networkUtil.httpRequest(url, "PUT", jsonInputString);

            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Change complete"));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Change complete"));

            }

        } catch (IOException e) {

            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Error updating customer on server. Message: " + e.getMessage()));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Error updating customer on server. Message: " + e.getMessage()));

            }

        }
    }

    /**
     * Updates a pasword on the server.
     * @param id The id of the customer.
     * @param password The new password of the customer.
     * @param type type defines admin or customer
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void>  updatePasswordCustomer (int id, String password,int type) {

        String url = BASE+"customers/" + "passwordChange";

        try {

            Customer c = new Customer();
            c.setPassword(password);
            c.setCustomerId(id);

            String jsonInputString = new Gson().toJson(c);

            networkUtil.httpRequest(url, "PUT",jsonInputString);

            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Change complete"));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Change complete"));

            }

        } catch (IOException e) {

            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Error updating customer on server. Message: " + e.getMessage()));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Error updating customer on server. Message: " + e.getMessage()));

            }

        }
    }



    /**
     * Deletes a customer on the serve.
     * @param id The id of the customer.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void>  deleteCustomer (int id) {

        String url = BASE+"customers";

        try {

            Customer c = new Customer();
            c.setCustomerId(id);

            String jsonInputString = new Gson().toJson(c);

            networkUtil.httpRequest(url, "DELETE",jsonInputString);

        } catch (IOException e) {
            return Trampoline.more(() -> tellerMainView.ShowMessage("Error deleting customer on server. Message: " + e.getMessage()));
        }
        return Trampoline.more(() -> tellerMainView.ShowMessage("Delete successful"));
    }


    /**
     * Setter for adminmainview.
     * @param tellerMainView The id of the customer.
     */
    public void setTellerMainView(TellerMainView tellerMainView) {
        this.tellerMainView = tellerMainView;
    }



    /**
     * Setter for customermainview.
     * @param customerMainView The id of the customer.
     */
     public void setCustomerMainView(CustomerMainView customerMainView) {
        this.customerMainView = customerMainView;
    }

    /**
     * LoadUser
     * @param currentUser => the user that is logged in.
     */
    public void loadUser(Customer currentUser) {
        this.customer= currentUser;
        getCustomer(currentUser.getCustomerId());
    }


    }





