package se.yrgo.dataaccess;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {

    private JdbcTemplate template;

    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    // 🔥 Skapa tabeller
    public void createTables() {
        try {
            template.update("""
                CREATE TABLE CUSTOMER (
                    CUSTOMER_ID VARCHAR(10),
                    NAME VARCHAR(100),
                    NOTES VARCHAR(255)
                )
            """);

            template.update("""
                CREATE TABLE CUSTOMER_CALL (
                    ID INTEGER IDENTITY,
                    NOTES VARCHAR(255),
                    TIME TIMESTAMP,
                    CUSTOMER_ID VARCHAR(10)
                )
            """);

        } catch (Exception e) {
            System.out.println("Tabeller finns redan");
        }
    }

    @Override
    public void create(Customer customer) {
        template.update(
                "INSERT INTO CUSTOMER VALUES (?,?,?)",
                customer.getCustomerId(),
                customer.getCompanyName(),
                customer.getNotes()
        );
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            return template.queryForObject(
                    "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?",
                    new CustomerMapper(),
                    customerId
            );
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return template.query("SELECT * FROM CUSTOMER", new CustomerMapper());
    }

    @Override
    public List<Customer> getByName(String name) {
        return template.query(
                "SELECT * FROM CUSTOMER WHERE NAME LIKE ?",
                new CustomerMapper(),
                "%" + name + "%"
        );
    }

    @Override
    public void update(Customer customer) throws RecordNotFoundException {
        template.update(
                "UPDATE CUSTOMER SET NAME=?, NOTES=? WHERE CUSTOMER_ID=?",
                customer.getCompanyName(),
                customer.getNotes(),
                customer.getCustomerId()
        );
    }

    @Override
    public void delete(Customer customer) throws RecordNotFoundException {
        template.update(
                "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?",
                customer.getCustomerId()
        );
    }

    @Override
    public void addCall(Call call, String customerId) throws RecordNotFoundException {
        template.update(
                "INSERT INTO CUSTOMER_CALL (NOTES, TIME, CUSTOMER_ID) VALUES (?,?,?)",
                call.getNotes(),
                new Timestamp(call.getTimeAndDate().getTime()),
                customerId
        );
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {

        Customer customer = getById(customerId);

        List<Call> calls = template.query(
                "SELECT NOTES, TIME FROM CUSTOMER_CALL WHERE CUSTOMER_ID=?",
                new CallMapper(),
                customerId
        );

        customer.setCalls(calls);

        return customer;
    }

    // 🔹 Mapper classes

    private static class CustomerMapper implements RowMapper<Customer> {
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
                    rs.getString("CUSTOMER_ID"),
                    rs.getString("NAME"),
                    rs.getString("NOTES")
            );
        }
    }

    private static class CallMapper implements RowMapper<Call> {
        public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Call(
                    rs.getString("NOTES"),
                    rs.getTimestamp("TIME")
            );
        }
    }
}