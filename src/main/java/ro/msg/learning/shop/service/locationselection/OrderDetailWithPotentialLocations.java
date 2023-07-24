package ro.msg.learning.shop.service.locationselection;

import lombok.Builder;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;

import java.util.Set;

@Builder
public record OrderDetailWithPotentialLocations(
        Set<Location> potentialLocations,
        Product product,
        Integer quantity
) {
}
