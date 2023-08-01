package ro.msg.learning.shop.service.locationselection;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Stock;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "ro.msg.learning.shop.locationselect.algorithm", havingValue = "MOST_ABUNDANT")
public class MostAbundantLocationSelectionAlgorithm implements LocationSelectionAlgorithm {
    @Override
    public Set<OrderDetail> selectLocationForItems(Address deliveryAddress, Collection<OrderDetailWithPotentialLocations> items) {
        return items.stream()
                .map(item -> new OrderDetail(
                        item.product(),
                        item.quantity(),
                        item.potentialLocations().stream()
                                .map(location ->
                                        location.getStocks().stream()
                                                .filter(s -> s.getProduct().equals(item.product())
                                                        && s.getQuantity() >= item.quantity())
                                                .max(Comparator.comparing(Stock::getQuantity))
                                                .map(stock -> new AbstractMap.SimpleImmutableEntry<>(location, stock.getQuantity())))
                                .flatMap(Optional::stream)
                                .max(Map.Entry.comparingByValue())
                                .map(AbstractMap.SimpleImmutableEntry::getKey)
                                .orElseThrow(() ->
                                        new LocationSelectionException(
                                                "No potential location has the product \"" + item.product().getId()
                                                        + "\" in stock in the quantity " + item.quantity()))
                        // This must not been thrown because the potential locations must have enough quantities of the product
                )).collect(Collectors.toSet());
    }
}
