package ro.msg.learning.shop.service;

import ro.msg.learning.shop.model.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findCustomerByUsername(String username);

    void registerCustomer(Customer customer);
}
