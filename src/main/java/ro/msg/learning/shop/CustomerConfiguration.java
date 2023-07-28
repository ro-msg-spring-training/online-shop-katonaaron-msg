package ro.msg.learning.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.repository.CustomerRepository;

@Configuration
public class CustomerConfiguration {
    /**
     * Returns a customer from the database, or a dummy customer if there is none
     *
     * @param customerRepository
     * @return
     */
    @Bean
    public Customer customer(CustomerRepository customerRepository) {
        return customerRepository.findAll().stream().findFirst()
                .orElseGet(() -> customerRepository.save(DummyData.johnSmith));
    }
}
