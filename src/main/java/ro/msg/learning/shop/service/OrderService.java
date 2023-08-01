package ro.msg.learning.shop.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.exception.LocationOutOfStockException;
import ro.msg.learning.shop.exception.ResourceNotFoundException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.service.locationselection.LocationSelectionAlgorithm;
import ro.msg.learning.shop.service.locationselection.OrderDetailWithPotentialLocations;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final LocationSelectionAlgorithm locationSelectionAlgorithm;

    @Transactional
    public Order createOrder(LocalDateTime timestamp,
                             Address deliveryAddress,
                             Map<UUID, Integer> productIdToQuantity,
                             Customer customer) {
        final var idToProduct = productRepository.findAllById(productIdToQuantity.keySet())
                .stream()
                .collect(Collectors.toMap(Product::getId, a -> a));

        final var items = productIdToQuantity.entrySet().stream()
                .map(entry -> {
                    final var productId = idToProduct.get(entry.getKey());
                    if (productId == null) {
                        throw new ResourceNotFoundException("Product with id \"" + entry.getKey() + "\" was not found");
                    }
                    return OrderDetailWithPotentialLocations.builder()
                            .potentialLocations(new HashSet<>(
                                    locationRepository.findByStocks_Product_IdAndStocks_QuantityGreaterThanEqual(
                                            entry.getKey(), entry.getValue())))
                            .product(productId)
                            .quantity(entry.getValue())
                            .build();
                })
                .toList();

        final var orderDetails = locationSelectionAlgorithm.selectLocationForItems(items);

        orderDetails.forEach(orderItem -> {
            removeProductFromStock(orderItem.getShippedFrom(), orderItem.getProduct(), orderItem.getQuantity());
            locationRepository.save(orderItem.getShippedFrom());
        });

        final var order = new Order();
        order.setCreatedAt(timestamp);
        order.setOrderDetails(orderDetails);
        order.setDeliveryAddress(deliveryAddress);
        order.setCustomer(customer);

        return orderRepository.saveAndFlush(order);
    }

    public Collection<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    private void removeProductFromStock(@NonNull Location location, @NonNull Product product, @NonNull Integer quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        final var stock = location.getStocks().stream()
                .filter(s -> s.getProduct().equals(product) && s.getQuantity() >= quantity)
                .findFirst()
                .orElseThrow(() -> new LocationOutOfStockException("Location \"" + location.getId()
                        + "\" does not have in stock product \"" + product.getId()
                        + "\" with quantity " + quantity));

        if (stock.getQuantity() > quantity) {
            stock.setQuantity(stock.getQuantity() - quantity);
        } else {
            location.getStocks().remove(stock);
        }
    }
}
