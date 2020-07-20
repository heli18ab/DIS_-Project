package view;

import controller.AccountController;
import controller.CustomerController;
import controller.MainController;
import cyclops.control.Trampoline;
import model.Customer;
import util.NetworkUtil;

import java.io.IOException;

public class MainView extends View {

    private CustomerController customerController = new CustomerController(new NetworkUtil());

    private AccountController accountController = new AccountController(new NetworkUtil());


    private TellerMainView tellerMainView = new TellerMainView(accountController, customerController);
    private CustomerMainView customerMainView = new CustomerMainView(accountController,customerController);

    private MainController mainController;

    public MainView(MainController controller) {
        this.mainController = controller;


    }




    public Trampoline<Void> returnToMainMenu() {
        try {
            System.out.println("Press any key to return to main menu");
            getNextStringValue();
            return Trampoline.more(this::loginMainView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Trampoline.more(this::loginMainView);
    }



    public Trampoline<Void> loginMainView()

    {
        while (true)
        {
            try {
                    System.out.println("--------------*******--------------");
                    System.out.println("              DØKBANK              ");
                    System.out.println("--------------*******--------------");

                    System.out.println("\nEnter username:");
                    String username = br.readLine();
                        System.out.println("Enter password");
                        String password = br.readLine();
                        return Trampoline.more(() -> mainController.loginVol(username, password));


            } catch (IOException ioe) {
                System.out.println("IO error trying to read your input!\r\n");
            }
        }
    }



    public Trampoline<Void> authorizeUser(Customer customer)
    {
        if(customer.getCustomerType() == 2){
            //customerController.loadUser(customer);
            System.out.println("------------********-----------");
            System.out.println ("Name: "+ customer.getName() + "\nCustomerId: " + customer.getCustomerId() + "\nYou are logged in as a teller");
            System.out.println("------------********-----------");
            return tellerMainView.TellerMainMenu();


        }

        else{
            //customerController.loadUser(customer);
            System.out.println("------------********-----------");
            System.out.println ("Name: "+ customer.getName() + "\nCustomerId: " + customer.getCustomerId() + "\nYou are logged in as a user");
            System.out.println("------------********-----------");
            customerMainView.setCurrentCustomer(customer);
            return customerMainView.CustomerMenu();
        }

    }







}
