package ro.msg.learning.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.service.CustomerService;

import static ro.msg.learning.shop.DummyData.johnSmith;

@Configuration
public class CustomerConfiguration {
    /**
     * Returns a customer from the database, or a dummy customer if there is none
     * @return
     */
    @Bean
    public Customer customer(CustomerService customerService) {
        return customerService.findCustomerByUsername(johnSmith.getUsername())
                .orElseGet(() -> customerService.registerCustomer(new Customer(johnSmith)));
    }
}
