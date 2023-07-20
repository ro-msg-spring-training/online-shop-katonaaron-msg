package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.AddressDTO;
import ro.msg.learning.shop.exception.OnlineShopException;
import ro.msg.learning.shop.mapper.AddressMapper;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LocationRepository locationRepository;
    private final AddressMapper addressMapper;
    private final Customer customer; // Dummy customer until Spring Security is integrated

    @Transactional
    public Order createOrder(LocalDateTime timestamp,
                             AddressDTO deliveryAddress,
                             Map<UUID, Integer> productIdToQuantity) {
        final var location = productIdToQuantity.entrySet().stream()
                .map(entry -> new HashSet<>(
                        locationRepository.findByStocks_Product_IdAndStocks_QuantityGreaterThanEqual(
                                entry.getKey(), entry.getValue())))
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .flatMap(s -> s.stream().min(Comparator.comparing(Location::getId)))
                .orElseThrow(() -> new OnlineShopException("No location was found which has the given products in stock in the given quantities"));

        location.getStocks().forEach(stock -> {
            final var quantityRequested = productIdToQuantity.get(stock.getProduct().getId());
            stock.setQuantity(stock.getQuantity() - quantityRequested);
        });
        location.getStocks().removeIf(stock -> stock.getQuantity() < 1);

        final var idToProduct = location.getStocks().stream()
                .collect(Collectors.toMap(s -> s.getProduct().getId(), Stock::getProduct));

        final var orderDetails = productIdToQuantity.entrySet().stream()
                .map(entry -> new OrderDetail(
                        idToProduct.get(entry.getKey()), // assume that it cannot be null because the location contains
                        // all the elements from the product list
                        entry.getValue()
                ))
                .collect(Collectors.toSet());

        final var order = new Order();
        order.setCreatedAt(timestamp);
        order.setOrderDetails(orderDetails);
        order.setShippedFrom(location);
        order.setDeliveryAddress(addressMapper.toEntity(deliveryAddress));
        order.setCustomer(customer);

        locationRepository.save(location);
        return orderRepository.saveAndFlush(order);
    }

    public Collection<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}
