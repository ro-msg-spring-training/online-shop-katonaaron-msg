package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class OrderDetail extends EntityWithId {
    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;
    private Integer quantity;
}
