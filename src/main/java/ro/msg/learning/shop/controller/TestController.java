package ro.msg.learning.shop.controller;

import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

@RestController
@Profile("integrationtest")
@AllArgsConstructor
@RequestMapping("/db")
public class TestController {
    private final Flyway flyway;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/clear")
    public void clearDB() {
        flyway.clean();
    }

    @PostMapping("/populate")
    public void populateDB() {
        flyway.migrate();
        setUp();
    }

    private void setUp() {
        customerRepository.save(DummyData.customer);
        supplierRepository.save(DummyData.supplier);
        productCategoryRepository.save(DummyData.category);
        productRepository.saveAll(DummyData.products);
        locationRepository.save(DummyData.location20);
    }

}
