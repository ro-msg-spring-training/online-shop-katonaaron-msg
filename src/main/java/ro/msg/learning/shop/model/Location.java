package ro.msg.learning.shop.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Location extends EntityWithId {
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "location")
    private Set<Stock> stocks;
}
