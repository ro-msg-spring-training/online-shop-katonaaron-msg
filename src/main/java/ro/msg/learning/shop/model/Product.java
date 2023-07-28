package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Product extends EntityWithId {
    private String name;
    private String description;
    private BigDecimal price;
    private Double weight;

    @ManyToOne
    private ProductCategory category;
    @ManyToOne
    private Supplier supplier;

    private String imageUrl;

    public Product(String name, String description, BigDecimal price, Double weight, ProductCategory category, Supplier supplier, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.category = category;
        this.supplier = supplier;
        this.imageUrl = imageUrl;
    }
}
