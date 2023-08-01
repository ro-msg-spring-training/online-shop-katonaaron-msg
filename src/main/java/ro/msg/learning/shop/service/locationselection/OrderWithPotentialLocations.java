package ro.msg.learning.shop.service.locationselection;

import lombok.Data;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderWithPotentialLocations {
    private Customer customer;
    private LocalDateTime createdAt;
    private Address deliveryAddress;
    private Set<OrderDetailWithPotentialLocations> orderDetails;

    public OrderWithPotentialLocations(Customer customer, LocalDateTime createdAt, Address deliveryAddress, Set<OrderDetailWithPotentialLocations> orderDetails) {
        this.customer = customer;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.orderDetails = orderDetails;
    }

    public Order toOrder(Set<OrderDetail> orderDetails) {
        return new Order(customer, createdAt, deliveryAddress, orderDetails);
    }
}
