package view;

import controller.AccountController;
import controller.CustomerController;
import cyclops.control.Trampoline;
import main.Main;
import model.Account;
import model.Customer;
import model.Transfer;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

/**
 * The main view of the application showing main menu etc.
 */
public class TellerMainView extends View {
    NetworkUtil networkUtil = new NetworkUtil();


    private CustomerController customerController = new CustomerController(networkUtil);
    private AccountController accountController = new AccountController(networkUtil);



    public TellerMainView(AccountController accountController, CustomerController customerController) {
        this.accountController = accountController;
        this.customerController = customerController;
        customerController.setTellerMainView(this);
        accountController.setTellerMainView(this);
    }

    public TellerMainView() {

    }

    public Trampoline<Void> TellerMainMenu() {

        while (true) {
            clearConsole();
            System.out.println("Main menu for teller:");
            System.out.println("[1] Get all customers");
            System.out.println("[2] Get customer");
            System.out.println("[3] Create a new customer");
            System.out.println("[3] Create a new teller");
            System.out.println("[4] Update a customer's information");
            System.out.println("[5] Delete a customer");
            System.out.println("[6] Make new account");
            System.out.println("[7] Show accounts");
            System.out.println("[8] Transfer money");
            System.out.println("[9] Withdraw");
            System.out.println("[10] Deposit");
            System.out.println("[11] Get all accounts and show total balance");
            System.out.print("Please select an option from 1-11\r\n");

            try {
                switch (getNextPosIntValue()) {
                    case 1:
                        return Trampoline.more(() -> customerController.getAllCustomers());
                    case 2:
                        try {

                            System.out.println("Enter user id");
                            int id = Integer.parseInt(br.readLine());
                            if (customerController.checkCustomer(id,2) == null) {
                                System.out.println("No user by that id");
                                returnToMainTellerMenu();
                            }
                            else{
                                return Trampoline.more(() -> customerController.getCustomer(id));
                            }
                            } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 3:
                        try {
                            System.out.println("Enter name on new user");
                            String name = br.readLine();
                            System.out.println("Enter username on new user");
                            String username = br.readLine();
                            System.out.println("Enter password on new user");
                            String password = br.readLine();
                            int type = 1;
                            return Trampoline.more(() -> customerController.createCustomer(name, username, password, type));
                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                        break;
                    case 4:
                        try {
                            System.out.println("Enter user id");
                            int id = Integer.parseInt(br.readLine());

                            if (customerController.checkCustomer(id,2) == null) {
                                System.out.println("No user by that id");
                                returnToMainTellerMenu();
                            }
                            else {
                                System.out.println("1. Endre brukernavn");
                                System.out.println("2. Endre passord");

                                switch (getNextPosIntValue()) {
                                    case 1:
                                        System.out.println("Enter new username");
                                        String username = br.readLine();
                                        return Trampoline.more(() -> customerController.updateUserName(id, username, 2));
                                    case 2:
                                        System.out.println("Enter new password");
                                        String password2 = br.readLine();
                                        return Trampoline.more(() -> customerController.updatePasswordCustomer(id, password2, 2));
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                        break;
                    case 5:
                        try {
                            System.out.println("Enter customerid you want to delete");
                            int id3 = Integer.parseInt(br.readLine());
                            if (customerController.checkCustomer(id3,2) == null) {
                                System.out.println("No user by that id");
                                returnToMainTellerMenu();
                            }
                            else{
                                return Trampoline.more(() -> customerController.deleteCustomer(id3));

                            }
                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                        break;
                    case 6:
                        try {
                            System.out.println("Enter customerid");
                            int id3 = Integer.parseInt(br.readLine());
                            if (customerController.checkCustomer(id3,2) == null) {
                                System.out.println("No user by that id");
                                returnToMainTellerMenu();
                            }
                            else{
                                System.out.println("Enter balance");
                                int balance = Integer.parseInt(br.readLine());
                                return Trampoline.more(() -> accountController.createAccount(id3, balance, 2));
                            }

                        } catch (Exception e) {
                            System.out.println("invalid input\nReturning to main menu");
                        }
                        break;
                    case 7:
                        try {
                            System.out.println("Enter customer id");
                            accountController.getAllAccounts();
                            int id3 = Integer.parseInt(br.readLine());

                            return Trampoline.more(() -> accountController.getAccounts(id3, 2));

                            } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 8:
                        System.out.println("Enter customer id");
                        int customerId = Integer.parseInt(br.readLine());

                        if (customerController.checkCustomer(customerId,2) == null) {
                            System.out.println("No user by that id");
                            returnToMainTellerMenu();
                        }
                        else{
                            ShowAccounts(customerId);
                            System.out.println("Account number you want to transfer from: ");
                            int accountId_from = Integer.parseInt(br.readLine());
                            if(accountController.getOneAccount(accountId_from,2) == null) {
                                System.out.println("No account by this id");
                                returnToMainTellerMenu();
                                break;
                            };

                            System.out.println("Account number you want to transfer to: ");
                            int accountId_to = Integer.parseInt(br.readLine());
                            if(accountController.getOneAccount(accountId_to,2) == null){
                                System.out.println("No account by this id");
                                returnToMainTellerMenu();
                            }
                            else{
                                System.out.println("Skriv inn beløpet du vil overføre");
                                int amount = Integer.parseInt(br.readLine());
                                Transfer transfer = new Transfer();
                                transfer.setAccount_from(accountId_from);
                                transfer.setAccount_to(accountId_to);
                                transfer.setAmount(amount);
                                return Trampoline.more(() -> accountController.transfer(transfer, 2));
                            }
                        }




                        break;
                    case 9:
                        Account account = new Account();
                        System.out.println("Skriv inn customerId");
                        int customerId1 = Integer.parseInt(br.readLine());
                        ShowAccounts(customerId1);
                        System.out.println("Skriv inn kontoen du vil trekke beløp fra");
                        int accountId = Integer.parseInt(br.readLine());


                        System.out.println("Skriv inn beløpet du vil trekke fra ");

                        int amount1 = Integer.parseInt(br.readLine());
                        double balanceAccount = accountController.getOneAccount(accountId, 2).getBalance();

                        if (balanceAccount > amount1) {
                            account.setAccount_id(accountId);
                            account.setBalance(+amount1);
                            return Trampoline.more(() -> accountController.withDrawAndDeposit(account, 2));
                        } else {
                            System.out.println("Your balance is to low ");
                            returnToMainTellerMenu();
                        }


                    case 10:
                        System.out.println("Enter customerid");
                        int id3 = Integer.parseInt(br.readLine());
                        if (customerController.checkCustomer(id3,2) == null) {
                            System.out.println("No user by that id");
                            returnToMainTellerMenu();

                        }

                        else {
                            ShowAccounts(id3);
                            System.out.println("Choose account you want to deposit money to");
                            int accountid = Integer.parseInt(br.readLine());
                            System.out.println("Write amount ");
                            int amount2 = Integer.parseInt(br.readLine());
                            Account account2 = new Account();
                            account2.setAccount_id(accountid);
                            account2.setBalance(-amount2);
                            return Trampoline.more(() -> accountController.withDrawAndDeposit(account2, 2));
                        }

                        break;
                    case 11:
                        return Trampoline.more(() -> accountController.getAllAccounts());

                    case 12:
                        int accountId3 = Integer.parseInt(br.readLine());
                        System.out.println(accountController.getOneAccount(accountId3, 2).getBalance());
                    default:
                        System.out.println("invalid selection. Returning to main menu\n");
                }
            } catch (IOException ioe) {
                System.out.println("IO error trying to read your input!\r\n");
            }
        }
    }



    /**
     * For returning to the main menu.
     * @return String to be allowed to return to another method. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> returnToMainTellerMenu() {
        try {
            System.out.println("\nPress any key to return to main menu");
            getNextStringValue();

            return Trampoline.more(this::TellerMainMenu);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Trampoline.more(this::TellerMainMenu);
    }

        /**
         * Shows a customer.
         * @param customer The customer to show.
         * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
         */
    public Trampoline<Void> ShowCustomer(Customer customer)
    {
        System.out.println(customer);
        return Trampoline.more(this::returnToMainTellerMenu);
    }

    /**
     * Shows all customers.
     * @param customers The customers to be shown.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> ShowAllCustomers(List<Customer> customers)
    {
        if(customers.size() == 0){
            System.out.println("No customers in the system ");
            return Trampoline.more(this::returnToMainTellerMenu);
        }
        else{
            System.out.println("---------------*************----------------");
            for(Customer c : customers) {
                System.out.println(c);
            }
            System.out.println("---------------*************----------------");

        }

        return Trampoline.more(this::returnToMainTellerMenu);
    }

    public void ShowAccounts(int userID) {
        for (Account a : accountController.returnAccounts(userID,2)) {
            System.out.println(a);
        }
    }
    public void showOneAccount(Account account) {
        System.out.println(account);
        returnToMainTellerMenu();
    }

    public Trampoline<Void> ShowAllAccounts(List<Account> accounts)
    {
        if(accounts.size() == 0){
            System.out.println("No accounts for that user");
            return Trampoline.more(this::returnToMainTellerMenu);
        }
        else{
            System.out.println("---------------*************----------------");
            for(Account a : accounts) {
                System.out.println(a);
            }
            System.out.println("---------------*************----------------");
        }


        return Trampoline.more(this::returnToMainTellerMenu);
    }




    public Trampoline<Void> ShowAllAccountsWithBalance(List<Account> accounts)

    {

        if(accounts.size() == 0){
            System.out.println("No accounts in the bank");
            return Trampoline.more(this::returnToMainTellerMenu);
        }
        double totalBalance = 0;
        System.out.println("---------------*************----------------");
        for(Account a : accounts) {
            System.out.println(a);
            totalBalance = totalBalance + a.getBalance();
        }
        System.out.println("---------------*************----------------");
        System.out.println("\nDen totale balance er: " + totalBalance);


        return Trampoline.more(this::returnToMainTellerMenu);
    }




    /**
     * Shows a sinxgle message, before returning to main menu.
     * @param message The message to show.
     * @return String to be allowed to return back to view. See readme.md note, to learn more (optional).
     */
    public Trampoline<Void> ShowMessage(String message)
    {
        System.out.println(message);
        return Trampoline.more(this::returnToMainTellerMenu);
    }


}
