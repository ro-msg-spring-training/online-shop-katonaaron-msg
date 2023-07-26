package ro.msg.learning.shop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.IntegrationTest;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.SupplierRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
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

    private static void checkOrder(Order order, LocalDateTime timestamp, Address address, Map<UUID, Integer> productIdToQuantity) {
        assertThat(order.getCreatedAt()).isEqualTo(timestamp);
        assertThat(order.getCustomer()).isEqualTo(DummyData.customer);
        assertThat(order.getDeliveryAddress()).isEqualTo(address);
        assertThat(order.getOrderDetails()).hasSize(productIdToQuantity.size())
                .allMatch(orderDetail -> {
                    var quantity = productIdToQuantity.get(orderDetail.getProduct().getId());
                    return quantity != null
                            && quantity.equals(orderDetail.getQuantity())
                            && DummyData.location20.equals(orderDetail.getShippedFrom());
                });
    }

    @Before
    public void setUp() {
        customerRepository.save(DummyData.customer);
        supplierRepository.save(DummyData.supplier);
        productCategoryRepository.save(DummyData.category);

        productRepository.saveAll(DummyData.products);

        locationRepository.save(DummyData.location20);
    }

    @Test
    public void testCreateOrder_sufficientStock_success() {
        Map<UUID, Integer> productIdToQuantity = Map.of(
                DummyData.product1.getId(), 10,
                DummyData.product2.getId(), 15
        );

        testOrderCreationSuccess(productIdToQuantity);
    }

    @Test
    public void testCreateOrder_insufficientStock_fail() {
        Map<UUID, Integer> productIdToQuantity = Map.of(
                DummyData.product1.getId(), 30,
                DummyData.product2.getId(), 15
        );
        testOrderCreationFail(productIdToQuantity);
    }

    private void testOrderCreationSuccess(Map<UUID, Integer> productIdToQuantity) {
        final var timestamp = LocalDateTime.now();
        final var address = DummyData.createAddress(0);
        final var order = orderService.createOrder(timestamp, address, productIdToQuantity);

        checkOrder(order, timestamp, address, productIdToQuantity);
    }

    private void testOrderCreationFail(Map<UUID, Integer> productIdToQuantity) {
        final var timestamp = LocalDateTime.now();
        final var address = DummyData.createAddress(0);

        assertThatThrownBy(() -> orderService.createOrder(timestamp, address, productIdToQuantity))
                .isInstanceOf(LocationSelectionException.class);
    }
}