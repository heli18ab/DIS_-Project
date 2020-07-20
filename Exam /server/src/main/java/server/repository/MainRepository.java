package server.repository;

import server.model.Account;
import server.model.Customer;
import server.util.Hashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainRepository extends BaseRepository {
    private Connection connection;
    private static BaseRepository dbCon;

    Hashing hashing;

    /**
     * Login function
     *
     * @param username The customer that wants to login
     *  @param password The customer that wants to login
     * @return customer that is logged in.
     */
    public Customer loginCustomer(String username, String password) throws SQLException {

        if(connection == null){
           connection=  getConnection();
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;
        try {
            String sql = "SELECT * FROM customers where username = ?  AND password = ?";
             ps = super.prepareQuery( sql, connection);


            ps.setString(1, username);
            ps.setString(2, password);

             rs = super.executePreparedStatementQuery(ps);

            if ((rs!=null &&rs.next()))
                customer = new Customer(rs.getString("name"), rs.getString("username"),rs.getString("password"), rs.getInt("customer_id"), rs.getInt("typeCustomer"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            cleanUp(rs,ps);
        }

        return customer;


        }

    }