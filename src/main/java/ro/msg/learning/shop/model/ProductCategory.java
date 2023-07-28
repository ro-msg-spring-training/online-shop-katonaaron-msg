package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ProductCategory extends EntityWithId {
    private String name;
    private String description;

    public ProductCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
