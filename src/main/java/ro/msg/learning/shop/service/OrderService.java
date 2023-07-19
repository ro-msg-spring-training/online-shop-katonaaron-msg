package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.CreateOrderDTO;
import ro.msg.learning.shop.dto.CreateOrderDetailDTO;
import ro.msg.learning.shop.exception.OnlineShopException;
import ro.msg.learning.shop.mapper.AddressMapper;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LocationRepository locationRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public Order createOrder(CreateOrderDTO createOrderDTO) {
        final var productIdToQuantity = createOrderDTO.orderDetails().stream()
                .collect(Collectors.toMap(CreateOrderDetailDTO::productId, CreateOrderDetailDTO::quantity));
        final var location = locationRepository.findAll().stream()
                .filter(loc ->
                        loc.getStocks().stream()
                                .filter(stock -> {
                                    final var quantity = productIdToQuantity.get(stock.getProduct().getId());
                                    return quantity != null && quantity <= stock.getQuantity();
                                })
                                .map(stock -> stock.getProduct().getId())
                                .collect(Collectors.toSet())
                                .equals(productIdToQuantity.keySet())
                ).findFirst()
                .orElseThrow(() -> new OnlineShopException("No location was found which has the given products in stock in the given quantities"));

        location.getStocks().forEach(stock -> {
            final var quantityRequested = productIdToQuantity.get(stock.getProduct().getId());
            stock.setQuantity(stock.getQuantity() - quantityRequested);
        });
        location.getStocks().removeIf(stock -> stock.getQuantity() < 1);

        final var idToProduct = location.getStocks().stream()
                .collect(Collectors.toMap(a -> a.getProduct().getId(), Stock::getProduct));

        final var orderDetails = createOrderDTO.orderDetails().stream()
                .map(dto -> new OrderDetail(
                        idToProduct.get(dto.productId()), // assume that it cannot be null because the location contains
                        // all the elements from the product list
                        dto.quantity()
                ))
                .collect(Collectors.toSet());

        final var order = new Order();
        order.setCreatedAt(createOrderDTO.timestamp());
        order.setOrderDetails(orderDetails);
        order.setShippedFrom(location);
        order.setDeliveryAddress(addressMapper.toEntity(createOrderDTO.deliveryAddress()));
        order.setCustomer(null); //TODO

        locationRepository.save(location);
        return orderRepository.saveAndFlush(order);
    }

    public Collection<Order> findAllOrders() {
        return orderRepository.findAll();
    }
}
