package ro.msg.learning.shop.model;

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
    private Location shippedFrom;

    @ManyToOne
    private Customer customer;

    private LocalDateTime createdAt;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;
}
