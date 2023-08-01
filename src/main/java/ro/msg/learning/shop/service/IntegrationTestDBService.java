package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

@Service
@RequiredArgsConstructor
@Profile("integrationtest")
public class IntegrationTestDBService {
    private final Flyway flyway;

    private final ProductRepository productRepository;

    private final SupplierRepository supplierRepository;

    private final LocationRepository locationRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final CustomerService customerService;

    @Transactional
    public void clearAndPopulateDB() {
        flyway.clean();
        flyway.migrate();
        setUp();
    }

    private void setUp() {
        DummyData.customers.stream()
                .map(Customer::new)
                .forEach(customerService::registerCustomer);
        supplierRepository.saveAll(DummyData.suppliers);
        productCategoryRepository.saveAll(DummyData.productCategories);
        productRepository.saveAll(DummyData.products);
        locationRepository.saveAll(DummyData.locations);
    }

}
