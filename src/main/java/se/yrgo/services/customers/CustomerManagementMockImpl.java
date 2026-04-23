package se.yrgo.services.customers;

import java.util.HashMap;
import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

public class CustomerManagementMockImpl implements CustomerManagementService {

    private HashMap<String, Customer> customerMap;

    public CustomerManagementMockImpl() {
        customerMap = new HashMap<>();
        customerMap.put("OB74", new Customer("OB74", "Fargo Ltd", "notes"));
        customerMap.put("NV10", new Customer("NV10", "North Ltd", "notes"));
        customerMap.put("RM210", new Customer("RM210", "River Ltd", "notes"));
    }

    @Override
    public void newCustomer(Customer newCustomer) {
        customerMap.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        customerMap.put(changedCustomer.getCustomerId(), changedCustomer);
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        customerMap.remove(oldCustomer.getCustomerId());
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        Customer c = customerMap.get(customerId);
        if (c == null) throw new CustomerNotFoundException();
        return c;
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        return customerMap.values().stream()
                .filter(c -> c.getCompanyName().contains(name))
                .toList();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerMap.values().stream().toList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        return findCustomerById(customerId);
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        Customer c = findCustomerById(customerId);
        c.addCall(callDetails);
    }
}