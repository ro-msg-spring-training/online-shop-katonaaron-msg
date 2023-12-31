package ro.msg.learning.shop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Orders")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Order extends EntityWithId {

    @ManyToOne
    private Customer customer;

    private LocalDateTime createdAt;

    @Embedded
    private Address deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    public Order(Customer customer, LocalDateTime createdAt, Address deliveryAddress, Set<OrderDetail> orderDetails) {
        this.customer = customer;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        setOrderDetails(orderDetails);
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        orderDetails.forEach(od -> od.setOrder(this));
    }
}
