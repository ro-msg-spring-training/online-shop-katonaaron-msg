package ro.msg.learning.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.repository.CustomerRepository;

@SpringBootApplication
@EnableTransactionManagement
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	/**
	 * Returns a customer from the database, or a dummy customer if there is none
	 *
	 * @param customerRepository
	 * @return
	 */
	@Bean
	Customer customer(CustomerRepository customerRepository) {
		final var dummyCustomer = new Customer();
		dummyCustomer.setUsername("test");
		dummyCustomer.setFirstName("test");
		dummyCustomer.setLastName("test");
		dummyCustomer.setEmailAddress("test@test.com");
		dummyCustomer.setPassword("testpassword");
		return customerRepository.findAll().stream().findFirst()
				.orElseGet(() -> customerRepository.save(dummyCustomer));
	}

}
