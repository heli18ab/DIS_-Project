package server.controllers;

import javax.ws.rs.*;

import Cache.AccountCache;
import com.google.gson.Gson;
import server.model.Account;
import server.model.Customer;
import server.model.Transfer;
import server.repository.AccountRepository;
import server.repository.MainRepository;
import server.util.Encryption;

import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Contains the endpoints for account operations.
 */

@Path("/accounts")
public class AccountController {
    public static AccountCache accountCache = new AccountCache();


    AccountRepository accountRepository;
    MainRepository mainRepository;


    public AccountController() {
        this.mainRepository = new MainRepository();
        this.accountRepository = new AccountRepository();
    }


    /**
     * Gets all accounts from the database
     *
     * @return Response to the client.
     */

    @GET
    public Response getAllAccounts(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie)throws SQLException {

        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        ArrayList<Account> accounts1 = accountCache.getAccounts(false);

        String output = new Gson().toJson(accounts1);
        output = Encryption.encryptDecrypt(output);
        output = new Gson().toJson(output);



        return Response
                .status(200)
                .type("application/json")
                .entity(output)
                .build();
    }


    /**
     * Gets a account by id.
     *
     * @param id The id of the account.
     * @return The response to the client.
     */
    @GET
    @Path("{spesificAccount}/{id}")
    public Response getAccount(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, @PathParam("id") int id) throws SQLException {



        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }



        Account account = accountCache.getOneAccountforCustomer(id);
        String output = new Gson().toJson(account);

        output = Encryption.encryptDecrypt(output);



        return Response
                .status(200)
                .type("application/json")
                .entity(output)
                .build();
    }


    /**
     * Gets all accounts from one customer from the database
     *
     * @return Response to the client.
     */
    @GET
    @Path("{id}")
    public Response getAccountsForCustomer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, @PathParam("id") int id) throws SQLException {

        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        ArrayList<Account> accounts = accountCache.getAccountsforCustomer(id);

        String output = new Gson().toJson(accounts);
        output = Encryption.encryptDecrypt(output);
         output = new Gson().toJson(output);


        return Response
                .status(200)
                .type(MediaType.TEXT_PLAIN)
                .entity(output)
                .build();
    }



    /**
     * Gets intrest function - create intrest on each account.
     *
     * @return Response to the client.
     */
    @GET
    @Path("/interest")
    public Response makeInterest() throws SQLException {
        ArrayList<Account> accounts;
        accounts = accountRepository.getAllAccounts();



        for(Account account : accounts){
            accountRepository.makeInterest(account);
        }
        return Response.status(200).
                build();
    }


    /**
     * Creates a account from a JSON encoded representation of a account object.
     *
     * @param account The JSON encoded representation of a account object.
     * @return A response indicating if the creation went well.
     */
    @POST
    public Response createAccount(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String account) throws SQLException {


        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }





        Account account1 = new Gson().fromJson(account, Account.class);


        accountRepository.createAccount(account1);

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"accountCreated\":\"true\"}")
                .build();
    }



    /**
     * Do an withdraw or deposit, updating an account.  from a JSON encoded representation of a account object.
     *
     * @param account The JSON encoded representation of a account object.
     * @return A response indicating if the creation went well.
     */
    @PUT
    public Response withDrawAndDeposit(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String account) throws SQLException {


        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }




        Account account1 = new Gson().fromJson(account, Account.class);
        accountRepository.withDrawAndDeposit(account1);  //kan redigere til en amount i metoden.
        String output = new Gson().toJson("Withdraw succeed ");
        output = Encryption.encryptDecrypt(output);

        return Response.
                status(200).
                type(MediaType.APPLICATION_JSON)
                .entity(output)
                .build();
    }


    /**
     * Do a transfer on two accounts.  from a JSON encoded representation of a account object.
     *
     * @param transfer The JSON encoded representation of a transfer object.
     * @return A response indicating if the creation went well.
     */

    @PUT
    @Path("{transfer}")
    public Response transfer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String transfer) throws SQLException {



        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        Transfer transfer1 = new Gson().fromJson(transfer, Transfer.class);

        accountRepository.transfer(transfer1);

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"transferCompleted\":\"true\"}")
                .build();
    }





}




/*
    @PUT
    @Path("{Interest}")
    public Response updateInterest() throws SQLException {
        accountRepository.addInterest();


        return Response
                .status(200)
                .type("application/json")
                .entity("{\"transferCompleted\":\"true\"}")
                .build();
    }
/*

 */
    /*
    @GET
    @Path("/cach")
    public Response getCach() throws SQLException {

      accountCache.getAccounts(false);

        return Response
                .status(200)
                .type("application/json")

                .build();
    }
*/






















