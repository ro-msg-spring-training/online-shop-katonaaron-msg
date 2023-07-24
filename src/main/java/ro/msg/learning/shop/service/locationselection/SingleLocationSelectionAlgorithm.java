package ro.msg.learning.shop.service.locationselection;

import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.OrderDetail;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class SingleLocationSelectionAlgorithm implements LocationSelectionAlgorithm {
    @Override
    public Set<OrderDetail> selectLocationForItems(Collection<OrderDetailWithPotentialLocations> items) {
        final var location = items.stream()
                .map(OrderDetailWithPotentialLocations::potentialLocations)
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .flatMap(s -> s.stream().min(Comparator.comparing(Location::getId)))
                .orElseThrow(() -> new LocationSelectionException("No location was found that has all the given products in stock in the given quantities"));
        return items.stream()
                .map(o -> new OrderDetail(o.product(), o.quantity(), location))
                .collect(Collectors.toSet());
    }
}
