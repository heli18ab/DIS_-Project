package server.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.model.Customer;
import server.repository.AccountRepository;
import server.repository.CustomerRepository;
import server.repository.MainRepository;
import server.util.Encryption;
import server.util.Hashing;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Contains endpoints for customer related operations.
 */
@Path("/customers")
public class CustomerController {

    private CustomerRepository customerRepository;
    private MainRepository mainRepository;

    Hashing hashing = new Hashing();

    public CustomerController() {
        this.mainRepository = new MainRepository();
        this.customerRepository = new CustomerRepository();
    }

    /**
     * Gets a customer by id.
     *
     * @param id The id of the customer.
     * @return The response to the client.
     */
    @GET
    @Path("{id}")
    public Response getCustomer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, @PathParam("id") int id) throws SQLException {

        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        Customer customer = customerRepository.getCustomer(id);
        String output = new Gson().toJson(customer);

        output = Encryption.encryptDecrypt(output);
        output = new Gson().toJson(output);


        return Response
                .status(200)
                .type("application/json")
                .entity(output)
                .build();
    }

    /**
     * Gets all customers from the database
     *
     * @return Response to the client.
     */
    @GET
    public Response getCustomers(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie) throws SQLException {

        /*@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie)*/
        // Get a list of users


        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }



        ArrayList<Customer> users = customerRepository.getCustomers();

        String out = new Gson().toJson(users);
        out = Encryption.encryptDecrypt(out);
        out = new Gson().toJson(out);

        // Return the users with the status code 200
        return Response.status(200).type(MediaType.TEXT_PLAIN).entity(out).build();
    }

    /**
     * Creates a customer from a JSON encoded representation of a customer object.
     *
     * @param customer The JSON encoded representation of a customer object.
     * @return A response indicating if the creation went well.
     */
    @POST
    public Response createCustomer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String customer) throws SQLException {



        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        Customer customer1 = new Gson().fromJson(customer, Customer.class);


        customerRepository.createCustomer(customer1);


        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userCreated\":\"true\"}")
                .build();
    }

    /**
     * Updates a customer from a JSON String representing a full JSON object.
     * Matches the customer based on the primary key.
     *
     * @param customer A JSON String representing a Customer object.
     * @return A JSON Object with e
     */
    @PUT
    @Path("usernameChange")
    public Response updateUserNameCustomer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String customer) throws SQLException {

        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        Customer customerToEdit = new Gson().fromJson(customer, Customer.class);
        customerRepository.updateUsernameCustomer(customerToEdit);

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userUpdated\":\"true\"}")
                .build();
    }

    @PUT
    @Path("passwordChange")
    public Response updatePasswordCustomer(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String customer) throws SQLException {


        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }

        Customer customerToEdit = new Gson().fromJson(customer, Customer.class);
        customerRepository.updatePasswordCustomer(customerToEdit);

        return Response
                .status(200)
                .type("application/json")
                .entity("{\"userUpdated\":\"true\"}")
                .build();
    }

    @DELETE
    public Response deleteUser(@CookieParam("Username") Cookie usernameCookie, @CookieParam("Password") Cookie passwordCookie, String body) throws SQLException {

        String username = usernameCookie.getValue();
        String password = passwordCookie.getValue();

        Customer c = mainRepository.loginCustomer(username, password);
        if (c == null) {
            return Response.status(400).build();
        }


        Customer customer = new Gson().fromJson(body, Customer.class);


        customerRepository.deleteCustomer(customer);
        return Response.status(200).type(MediaType.APPLICATION_JSON_TYPE).entity("The user is now deleted").build();
    }


    @POST
    @Path("login")
    public Response loginCustomer(String in) throws SQLException {
        JsonObject js = new Gson().fromJson(in, JsonObject.class);

        String username = new Gson().fromJson(js, JsonObject.class).get("username").getAsString();
        String password = new Gson().fromJson(js, JsonObject.class).get("password").getAsString();


        Customer customer = mainRepository.loginCustomer(username, hashing.hashWithSalt(password));

        String output = new Gson().toJson(customer);
        // output = Encryption.encryptDecrypt(output);

        System.out.println(output + "er logget inn");

        if (customer == null) {
            return Response
                    .status(200)
                    .type("application/json")
                    .entity("Access Denied")
                    .build();
        }

        return Response
                .status(200)
                .type("application/json")
                .entity(output)
                .build();
    }


}
