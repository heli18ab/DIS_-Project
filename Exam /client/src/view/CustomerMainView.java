package view;

import controller.AccountController;
import controller.CustomerController;
import cyclops.control.Trampoline;
import model.Account;
import model.Customer;
import model.Transfer;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class CustomerMainView extends View {

    NetworkUtil networkUtil = new NetworkUtil();

    private CustomerController customerController = new CustomerController(networkUtil);
    private AccountController accountController = new AccountController(networkUtil);


    private Customer currentCustomer;


    TellerMainView tellerMainView = new TellerMainView();


    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public CustomerMainView(AccountController accountController, CustomerController customerController) {

        this.accountController = accountController;
        this.customerController = customerController;
        customerController.setCustomerMainView(this);
        accountController.setCustomerMainView(this);

    }

    //main menu for customers. Handles all the function a customer can use.
    public Trampoline<Void> CustomerMenu() {

        while (true) {
            clearConsole();
            System.out.println("Main menu for a user:");
            System.out.println("[1]  Update Info");
            System.out.println("[2]  Make Acount");
            System.out.println("[3]  Show Accounts");
            System.out.println("[4]  Get spesific account");
            System.out.println("[5]  Transfer money");
            System.out.println("[6]  Withdraw");
            System.out.println("[7]  Deposit");
            System.out.print("\nPlease select an option from 1-7\r\n");



            try {
                switch (getNextPosIntValue()) {

                    case 1:
                        try {
                            System.out.println("-------********--------");
                            System.out.println("[1] Endre brukernavn");
                            System.out.println("[2] Endre passord");
                            System.out.println("-------********--------");

                            switch (getNextPosIntValue()) {
                                case 1:
                                    System.out.println("Enter new username");
                                    String username = br.readLine();
                                    return Trampoline.more(() -> customerController.updateUserName(currentCustomer.getCustomerId(), username, 1));
                                case 2:
                                    System.out.println("Enter new password");
                                    String password2 = br.readLine();
                                    return Trampoline.more(() -> customerController.updatePasswordCustomer(currentCustomer.getCustomerId(), password2,1));
                            }
                        }catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                            case 2:
                                System.out.println("Enter balance on your new account");
                                int balance = Integer.parseInt(br.readLine());
                                return Trampoline.more(() -> accountController.createAccount(currentCustomer.getCustomerId(), balance, 1));

                            case 3:
                                try {
                                    accountController.getAllAccounts();
                                    return Trampoline.more(() -> accountController.getAccounts(currentCustomer.getCustomerId(), 1));
                                } catch (Exception e) {
                                    System.out.println("invalid input\nReturning to main menu");
                                }
                                break;

                    case 4:

                        try {
                            System.out.println("Skriv inn accuntid du vil hente konto til");
                            int account_id = Integer.parseInt(br.readLine());
                            Account account1 =  accountController.getOneAccount(account_id, 1);
                            System.out.println(account1);
                            returnToCustomerMenu();
                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                            case 5:
                                ShowAccounts(currentCustomer.getCustomerId());

                                System.out.println("Skriv inn kontoen du vil overføre penger fra");
                                int accountId_from = Integer.parseInt(br.readLine());

                                System.out.println("Skriv inn accuntid du vil sette penger til");
                                int accountId_to = Integer.parseInt(br.readLine());

                                System.out.println("Skriv inn beløpet du vil overføre");
                                int amount = Integer.parseInt(br.readLine());

                                if (accountController.getOneAccount(accountId_from, 1) == null || accountController.getOneAccount(accountId_to, 1) == null) {
                                    System.out.println("Kontoen finnes ikke");
                                    returnToCustomerMenu();
                                } else {
                                    Transfer transfer = new Transfer();
                                    transfer.setAccount_from(accountId_from);
                                    transfer.setAccount_to(accountId_to);
                                    transfer.setAmount(amount);
                                    return Trampoline.more(() -> accountController.transfer(transfer, 1));
                                }


                            case 6:
                                tellerMainView.ShowAccounts(currentCustomer.getCustomerId());
                               System.out.println("Skriv inn kontoen du vil trekke beløp fra");
                                int accountId = Integer.parseInt(br.readLine());

                                System.out.println("Skriv inn beløpet du vil trekke fra ");
                                int amount1 = Integer.parseInt(br.readLine());
                                Account account = new Account();
                                account.setAccount_id(accountId);
                                account.setBalance(+amount1);

                                return Trampoline.more(() -> accountController.withDrawAndDeposit(account, 1));

                            case 7:
                                tellerMainView.ShowAccounts(currentCustomer.getCustomerId());
                                System.out.println("Skriv inn kontoen du vil sette inn penger på");
                                int accountid = Integer.parseInt(br.readLine());

                                System.out.println("Skriv inn beløpet du vil sette inn ");
                               int amount2 = Integer.parseInt(br.readLine());
                                Account account2 = new Account();
                                account2.setAccount_id(accountid);
                                account2.setBalance(-amount2);
                                return Trampoline.more(() -> accountController.withDrawAndDeposit(account2, 1));
                            default:
                                System.out.println("invalid selection. Returning to main menu\n");

                    case 8:
                        return Trampoline.more(() -> accountController.getAllAccounts());



                        }

                } catch(IOException ioe){
                    System.out.println("IO error trying to read your input!\r\n");
                }
            }
        }


    /**
     * Shows all customers.
     * @return String to be allowed to return back to menu.
     */
    public Trampoline<Void> returnToCustomerMenu() {
        try {
            System.out.println("\n--------------*************-------------------");
            System.out.println("Press any key to return to main menu");
            System.out.println("----------------*************-----------------");


            return Trampoline.more(this::CustomerMenu);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Trampoline.more(this::CustomerMenu);
    }





    /**
     * Shows all accounts for one customer.
     */
    public void ShowAccounts(int userID) {

        for (Account a : accountController.returnAccounts(userID,1)) {
            System.out.println(a);
        }
    }
    public void showOneAccount(Account account) {
        System.out.println(account);
        returnToCustomerMenu();
    }

    /**
     * Shows all customers.
     * @param accounts The customers to be shown.
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> ShowAllAccounts(List<Account> accounts)
    {
        if(accounts.size() == 0){
            System.out.println("No accounts for that user");
            return Trampoline.more(this::returnToCustomerMenu);
        }
        else{
            System.out.println("---------------*************----------------");
            for(Account a : accounts) {
                System.out.println(a);
            }
            System.out.println("---------------*************----------------");
        }

        return Trampoline.more(this::returnToCustomerMenu);
    }



    /**
     * Shows all customers.
     * @param message Prints a message
     * @return String to be allowed to return back to view.
     */
    public Trampoline<Void> ShowMessage(String message)
    {
        System.out.println(message);
        return Trampoline.more(this::returnToCustomerMenu);
    }




}
