package ro.msg.learning.shop.service.locationselection;

import lombok.Builder;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;

import java.util.Set;

@Builder
public record OrderDetailWithPotentialLocations(
        Product product,
        Integer quantity,
        Set<Location> potentialLocations
) {
}
