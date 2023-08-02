package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Revenue extends EntityWithId {
    @ManyToOne
    private Location location;

    private LocalDate date;

    private BigDecimal sum;

    public Revenue(Location location, LocalDate date, BigDecimal sum) {
        this.location = location;
        this.date = date;
        this.sum = sum;
    }
}
