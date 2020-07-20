package server.repository;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import server.model.Customer;
import server.util.Hashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A repository class for database operations for customer objects.
 */
public class CustomerRepository extends BaseRepository {
private Connection connection;

    /**
     * Creates a customer in the database.
     *
     * @param customer The customer to save to the database.
     * @return The customer with the primary key added.
     */
    public Customer createCustomer(Customer customer) throws SQLException {

        if(connection== null){
           connection=getConnection();
        }

        String sql = "INSERT INTO customers (name, username, password, typeCustomer) VALUES (?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {

        connection.setAutoCommit(false);

            ps = prepareInsert(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getUsername());
            ps.setString(3, customer.getPassword());
            ps.setInt(4, customer.getCustomerType());
            int primary_key = executeInsertPreparedStatement(ps);
            customer.setCustomerId(primary_key);

            connection.commit();
            connection.setAutoCommit(true);


        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }



        } finally {
            cleanUp(ps);
        }
        // Return the new customer
        return customer;
    }

    /**
     * Updates a username to a customer in the database based on the primary key.
     *
     * @param customer The customer to update.
     */
    public Customer updateUsernameCustomer(Customer customer) throws SQLException {
        if(connection== null){
            connection=getConnection();
        }

        String sql = "UPDATE customers SET username = ? WHERE customer_id = ?";

        PreparedStatement ps = null;
        Connection conn = null;

        try {

       connection.setAutoCommit(false);
            ps = prepareQuery( sql, connection);
            ps.setString(1,customer.getUsername());
            ps.setInt(2, customer.getCustomerId());
            executePreparedStatementQuery(ps);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            cleanUp(ps);
        }
        return customer;
    }

    /**
     * Updates a password to a customer in the database based on the primary key.
     *
     * @param customer The customer to update.
     */
    public Customer updatePasswordCustomer(Customer customer) throws SQLException {
        if(connection ==null){
            connection=getConnection();
        }

        String sql = "UPDATE customers SET password = ? WHERE customer_id = ?";

        PreparedStatement ps = null;

        try {
            connection.setAutoCommit(false);
            ps = prepareQuery( sql, connection);
            ps.setString(1,customer.getPassword());
            ps.setInt(2, customer.getCustomerId());
            executePreparedStatementQuery(ps);
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            cleanUp(ps);
        }
        return customer;
    }


    /**
     * Gets all customers from the database.
     *
     * @return A list of customers.
     */

    public ArrayList<Customer> getCustomers() throws SQLException {


        String sql = "SELECT * FROM customers";
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<Customer> customers = new ArrayList<>();

        ps= super.prepareQuery(sql);


        try {
            rs = executePreparedStatementQuery(ps);
            if (rs == null)
                return customers;
            while (rs.next()) {
                Customer customer =
                        new Customer(
                                rs.getString("name"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getInt("customer_id"),
                                rs.getInt("typeCustomer"));
                customers.add(customer);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }

        return customers;
    }

    /**
     * Gets a customer from the database by id.
     *
     * @param customer_id The id of the customer.
     * @return The customer, if found. Else null.
     */
    public Customer getCustomer(int customer_id) throws SQLException {
        if(connection== null){
            connection=getConnection();
        }



        Customer customer = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String sql = "SELECT * FROM customers WHERE customer_id = ?";
            ps = super.prepareQuery(sql, connection);

            ps.setInt(1, customer_id);
            rs = super.executePreparedStatementQuery(ps);

            if (rs != null && rs.next())
                customer = new Customer(rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getInt("customer_id"), rs.getInt("typeCustomer"));

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            cleanUp(rs, ps);
        }


        return customer;
    }

    /**
     * Delets a customer in the database based on the primary key.
     *
     * @param customer The customer to delete.
     */
    public Customer deleteCustomer(Customer customer) throws SQLException {

        if(connection== null){
            connection=getConnection();
        }

        String sql = "DELETE FROM accounts WHERE customer_id = ?";
        String sql1 = "DELETE FROM customers WHERE customer_id = ?";

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        Connection conn = null;

        try {
          connection.setAutoCommit(false);


            ps = prepareQuery(sql,  connection);
            ps.setInt(1, customer.getCustomerId());

            ps1 = prepareQuery(sql1,  connection);
            ps1.setInt(1, customer.getCustomerId());



            ps.executeUpdate();
            ps1.executeUpdate();

           connection.commit();
           connection.setAutoCommit(true);

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            cleanUp(ps);
        }
        return customer;
    }
}

