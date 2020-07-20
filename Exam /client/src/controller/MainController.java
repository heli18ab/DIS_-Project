package controller;

import com.google.gson.Gson;
import cyclops.control.Trampoline;
import model.Customer;
import util.Encryption;
import util.NetworkUtil;
import view.MainView;

import java.io.IOException;


public class MainController {

    private MainView mainView;
    private NetworkUtil networkUtil;

     AccountController accountController;
     CustomerController customerController;

    private static final String BASE = "http://localhost:8080/exam/";
    public static Customer currentCustomer = null;



    public MainController(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        accountController = new AccountController(networkUtil);
        customerController = new CustomerController(networkUtil);


    }

    public MainController(){

    }



    /**
     *Login, checks if the logged in customer exists in database
     *@param username
     * @param password
     * @return to mainView and authorizeUser
     */
    public Trampoline<Void> loginVol(String username, String password){
        try {
            String url = BASE + "customers/" + "login/";

            Customer c = new Customer();
            c.setUsername(username);
            c.setPassword(password);
            String jsonInputString = new Gson().toJson(c);


            String response = networkUtil.httpRequestLogin(url, "POST",jsonInputString);
             NetworkUtil networkUtil = new NetworkUtil();

            //String response1 = Encryption.encryptDecrypt(response);

            Customer customer1 = new Gson().fromJson(response, Customer.class);
            if(customer1 != null) {
                this.currentCustomer = customer1;
            }


            AccountController accountController = new AccountController(networkUtil);
            accountController.getAllAccounts();

            return Trampoline.more(() -> mainView.authorizeUser(customer1));

        } catch (Exception e ) {
            System.out.println("Wrong username or password");
            return Trampoline.more(() -> mainView.loginMainView());


        }



    }

    public static Customer getCurrentCustomer(){
        return currentCustomer;
    }



    /*
    Method that sets interst- can be used if intrest file is replaced.
    public void setIntrest() {
        try{
            String url = BASE + "accounts/" + "interest";
            networkUtil.httpRequest(url, "PUT");

        } catch (IOException e) {

        }
    }
*/

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }



}





