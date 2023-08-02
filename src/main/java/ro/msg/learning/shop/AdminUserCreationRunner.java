package ro.msg.learning.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.service.CustomerService;

@Component
@RequiredArgsConstructor
public class AdminUserCreationRunner implements CommandLineRunner {
    private final CustomerService customerService;

    @Value("${ro.msg.learning.shop.admin.username}")
    private String adminUsername;
    @Value("${ro.msg.learning.shop.admin.password}")
    private String adminPassword;
    @Value("${ro.msg.learning.shop.admin.firstname}")
    private String adminFirstName;
    @Value("${ro.msg.learning.shop.admin.lastname}")
    private String adminLastName;
    @Value("${ro.msg.learning.shop.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (customerService.findCustomerByUsername(adminUsername).isEmpty()) {
            customerService.registerCustomer(new Customer(
                    adminFirstName, adminLastName,
                    adminUsername,
                    adminPassword,
                    adminEmail
            ));
        }
    }
}
