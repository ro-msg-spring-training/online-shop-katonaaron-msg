package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.repository.CustomerRepository;
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

    private final CustomerRepository customerRepository;


    public void clearDB() {
        flyway.clean();
    }

    public void populateDB() {
        flyway.migrate();
        setUp();
    }

    private void setUp() {
        customerRepository.save(DummyData.customer);
        supplierRepository.save(DummyData.supplier);
        productCategoryRepository.save(DummyData.category);
        productRepository.saveAll(DummyData.products);
        locationRepository.save(DummyData.locationWithStock20);
    }

}
