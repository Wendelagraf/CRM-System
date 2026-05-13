package se.yrgo.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.domain.Customer;
import se.yrgo.services.customers.CustomerManagementService;

public class SimpleClient {

    public static void main(String[] args) {

        try {
            ApplicationContext context =

                    new ClassPathXmlApplicationContext("application.xml");

            CustomerManagementService service =
                    context.getBean(CustomerManagementService.class);

            Customer c1 = new Customer("C1", "Test AB", "notes");
            service.newCustomer(c1);

            service.getAllCustomers().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}