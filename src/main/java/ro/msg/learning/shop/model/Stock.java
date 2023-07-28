package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Stock extends EntityWithId {
    @ManyToOne
    private Product product;

    @ManyToOne
    @ToString.Exclude
    private Location location;

    private Integer quantity;

    public Stock(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
