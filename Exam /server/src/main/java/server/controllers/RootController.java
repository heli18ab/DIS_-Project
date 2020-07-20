package server.controllers;

import com.google.gson.Gson;
import server.model.Customer;
import server.repository.MainRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/login")
public class RootController {


    MainRepository mainRepository = new MainRepository();


    @GET
    public Response defaultGetMethod(){
        return Response.status(200).type("text/html").entity("Welcome to DIS exam 2019. Good luck!").build();
    }


}