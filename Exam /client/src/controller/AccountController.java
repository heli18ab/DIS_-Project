package controller;

import com.google.gson.Gson;
import cyclops.control.Trampoline;

import model.Account;
import model.Transfer;
import util.Encryption;
import util.NetworkUtil;
import view.TellerMainView;
import view.CustomerMainView;

import java.io.IOException;
import java.util.Arrays;

public class AccountController extends MainController {

    //declaration of classes that will be used later
    private NetworkUtil networkUtil;
    private TellerMainView tellerMainView;
    private CustomerMainView customerMainView;


    private static final String BASE = "http://localhost:8080/exam/";

    public AccountController(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }




    /**
     * Gets a spesific account on the server.
     * @param accountId The spesific accountId of the customer.
     *  @param type defines if user is an admin or a customer.
     * @return account
     */
    public Account getOneAccount(int accountId, int type) {
        try {


            String url = BASE + "accounts/" + "spesificAccount/"+  (accountId);
            String response = networkUtil.httpRequest(url, "GET");
            response = Encryption.encryptDecrypt(response);

            Account account = new Gson().fromJson(response, Account.class);
             return account;
        } catch (IOException e) {
            if (type == 2) {
                tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());
            } else
                customerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());
        }
            return null;
        }




    /**
     * Gets all accounts on the server.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> getAllAccounts() {
        try {
            String url = BASE + "accounts";


            String response = networkUtil.httpRequest(url, "GET");

            response = new Gson().fromJson(response, String.class);
            response = Encryption.encryptDecrypt(response);

            Account[] accounts = new Gson().fromJson(response, Account[].class);


            return Trampoline.more(() -> tellerMainView.ShowAllAccountsWithBalance(Arrays.asList(accounts)));
        } catch (IOException e) {
            return Trampoline.more(() -> tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage()));
        }

    }



    /**
     * Get accounts from a spesfic user.
     * @param userId The id of the customer.
     * @param type defines if user is an admin or a customer.
     * @return String to be allowed to return back to view with an arraylist.
     */
    public Trampoline<Void> getAccounts(int userId, int type) {
        try {
            String url = BASE + "accounts/" + (userId);

            String response = networkUtil.httpRequest(url, "GET");

            response = new Gson().fromJson(response, String.class);
            response = Encryption.encryptDecrypt(response);

            Account[] accounts = new Gson().fromJson(response, Account[].class);


            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowAllAccounts(Arrays.asList(accounts)));
            } else {
                return Trampoline.more(() -> customerMainView.ShowAllAccounts(Arrays.asList(accounts)));

            }
        } catch (IOException e) {
            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage()));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage()));

            }

        }
    }

    /**
     * Get accounts from a spesfic user.
     * @param userId The id of the customer.
     *  @param type defines if user is an admin or a customer.
     * @return accounts : all accounts in an arraylist.
            */
    public Account[] returnAccounts(int userId, int type) {
        try {

            String url = BASE + "accounts/" + (userId);
            String response = networkUtil.httpRequest(url, "GET");

            response = new Gson().fromJson(response, String.class);
            response = Encryption.encryptDecrypt(response);


            Account[] accounts = new Gson().fromJson(response, Account[].class);


            return accounts;
        } catch (IOException e) {

            if(type == 2) {
                tellerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());
            }

            else{
                customerMainView.ShowMessage("Error getting customer from server. Message: " + e.getMessage());

            }
        }
        return null;
    }



    /**
     * Creates a accounts on the server.
     * @param customer_id => id on the new user
     * @param balance => sets  balance on user.
     * @param type defines if user is an admin or a customer.
     * @return String to be allowed to return back to view.
     */
        public Trampoline<Void>  createAccount(int customer_id, int balance, int type) {
            String url = BASE+"accounts";
            try {
                Account a = new Account();
                a.setCustomer_id(customer_id);
                a.setBalance(balance);
                String jsonInputString = new Gson().toJson(a);

                //TODO: Handle the reply from the server correctly here.
                networkUtil.httpRequest(url, "POST",jsonInputString);

            } catch (Exception e) {

                    return Trampoline.more(() -> tellerMainView.ShowMessage("Error creating account. Message: " + e.getMessage()));
            }
            if(type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Account created. "));
            }
            else{
                return Trampoline.more(() -> customerMainView.ShowMessage("Account created. "));

            }
        }


    /**
     * Conducts withdraw and deposit
     * @param account
     * @param type defines if user is an admin or a customer.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> withDrawAndDeposit(Account account, int type) {
        String url = BASE + "accounts";

        try {
            String request = new Gson().toJson(account);
            networkUtil.httpRequest(url, "PUT", request);


            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage(" Complete. "));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage(" Complete. "));

            }


        } catch (Exception e) {
            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Error with withdraw. Message: " + e.getMessage()));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Error with withdraw. Message: " + e.getMessage()));

            }


        }
    }


    /**
     * Conducts withdraw and deposit - transfer.
     * @param transfer
     * @param type defines if user is an admin or a customer.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> transfer(Transfer transfer, int type) {
        String url = BASE + "accounts" + "/transfer";

        try {
            String request = new Gson().toJson(transfer);
            networkUtil.httpRequest(url, "PUT", request);

            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Transfer complete. "));

            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Transfer complete. "));

            }

        } catch (Exception e) {
            if (type == 2) {
                return Trampoline.more(() -> tellerMainView.ShowMessage("Error with withdraw. Message: " + e.getMessage()));
            } else {
                return Trampoline.more(() -> customerMainView.ShowMessage("Error with withdraw. Message: " + e.getMessage()));

            }


        }



    }

/*
//method not in use - can replace intrest file.
    public void setIntrest() {
            try{
            String url = BASE + "accounts/" + "interest";
            networkUtil.httpRequest(url, "GET");

        } catch (IOException e) {

        }
    }
*/

    /**
     * Setter for tellerMainView.
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
}
