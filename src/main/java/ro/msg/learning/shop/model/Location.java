package ro.msg.learning.shop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ro.msg.learning.shop.exception.LocationOutOfStockException;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Location extends EntityWithId {
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private Set<Stock> stocks;

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
        stocks.forEach(stock -> stock.setLocation(this));
    }

    public void removeProductFromStock(@NonNull Product product, @NonNull Integer quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be positive");
        }

        final var stock = stocks.stream()
                .filter(s -> s.getProduct().equals(product) && s.getQuantity() >= quantity)
                .findFirst()
                .orElseThrow(() -> new LocationOutOfStockException(getId(), product.getId(), quantity));

        if (stock.getQuantity() > quantity) {
            stock.setQuantity(stock.getQuantity() - quantity);
        } else {
            stocks.remove(stock);
        }
    }
}
