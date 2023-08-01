package ro.msg.learning.shop.service.locationselection;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.OrderDetail;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ro.msg.learning.shop.locationselect.algorithm", havingValue = "SINGLE_LOCATION")
public class SingleLocationSelectionAlgorithm implements LocationSelectionAlgorithm {
    @Override
    public Set<OrderDetail> selectLocationForItems(OrderWithPotentialLocations order) {
        final var location = order.getOrderDetails().stream()
                .map(OrderDetailWithPotentialLocations::potentialLocations)
                .map(HashSet::new)
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .flatMap(s -> s.stream().min(Comparator.comparing(Location::getId)))
                .orElseThrow(() -> new LocationSelectionException("No location was found that has all the given products in stock in the given quantities"));
        return order.getOrderDetails().stream()
                .map(o -> new OrderDetail(o.product(), o.quantity(), location))
                .collect(Collectors.toSet());
    }
}
