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
public class Stock extends EntityWithId {
    @ManyToOne
    private Product product;

    @ManyToOne
    private Location location;

    private Integer quantity;
}
