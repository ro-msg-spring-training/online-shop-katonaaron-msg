package ro.msg.learning.shop.service;

import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Order;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    @Transactional
    Order createOrder(LocalDateTime timestamp,
                      Address deliveryAddress,
                      Map<UUID, Integer> productIdToQuantity,
                      Customer customer);

    Collection<Order> findAllOrders();
}
