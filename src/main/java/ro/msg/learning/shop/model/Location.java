package ro.msg.learning.shop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, of = "name")
@NoArgsConstructor
public class Location extends EntityWithId {
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private Set<Stock> stocks;

    public Location(String name, Address address, Set<Stock> stocks) {
        this.name = name;
        this.address = address;
        setStocks(stocks);
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
        stocks.forEach(stock -> stock.setLocation(this));
    }
}
