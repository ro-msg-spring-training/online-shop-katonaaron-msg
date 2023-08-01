package ro.msg.learning.shop.service.locationselection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.exception.OnlineShopInternalException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.OrderDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

@Component
@ConditionalOnProperty(name = "ro.msg.learning.shop.locationselect.algorithm", havingValue = "PRIORITY_BASED")
public class ProximityBasedLocationSelectionAlgorithm implements LocationSelectionAlgorithm {
    @Value("${ro.msg.learning.shop.locationselect.routematrix.apikey}")
    private String routeMatrixApiKey;
    @Value("${ro.msg.learning.shop.locationselect.routematrix.url}")
    private String routeMatrixUrl;

    @Override
    public Set<OrderDetail> selectLocationForItems(Address deliveryAddress, Collection<OrderDetailWithPotentialLocations> items) {
        final var locations = items.stream()
                .flatMap(od -> od.potentialLocations().stream())
                .collect(toCollection(ArrayList::new));

        final var productToQuantity = items.stream()
                .collect(toMap(OrderDetailWithPotentialLocations::product,
                        OrderDetailWithPotentialLocations::quantity));

        final var orderDetails = new HashSet<OrderDetail>();

        while (!productToQuantity.isEmpty()) {
            if (locations.isEmpty()) {
                throw new LocationSelectionException("Cannot find locations for all products");
            }

            final var addresses = Stream.concat(
                    Stream.of(deliveryAddress),
                    locations.stream()
                            .map(Location::getAddress)
            ).toList();

            final var distances = getDistances(addresses);

            int minIndex = IntStream.range(1, distances.size()).boxed()
                    .min(comparingLong(distances::get))
                    .orElseThrow(() -> new LocationSelectionException("Cannot find locations for all products"));

            final var closestLocation = locations.get(minIndex - 1);

            locations.remove(closestLocation);

            closestLocation.getStocks().stream()
                    .filter(stock -> productToQuantity.containsKey(stock.getProduct()))
                    .map(stock -> {
                        final var product = stock.getProduct();
                        final var requiredQuantity = productToQuantity.get(product);
                        final var obtainedQuantity = Math.min(stock.getQuantity(), requiredQuantity);

                        if (requiredQuantity == obtainedQuantity) {
                            productToQuantity.remove(product);
                        } else {
                            productToQuantity.replace(product, requiredQuantity - obtainedQuantity);
                        }

                        return new OrderDetail(product, obtainedQuantity, closestLocation);
                    })
                    .collect(toCollection(() -> orderDetails));
        }
        return orderDetails;
    }

    private List<Long> getDistances(List<Address> addresses) {
        final var restTemplate = new RestTemplate();

        final var request = new HttpEntity<>(new RequestDto(
                addresses.stream()
                        .map(this::addressToString)
                        .toList(),
                true
        ));

        ResponseDto response = restTemplate.postForObject(
                routeMatrixUrl + "?key=" + routeMatrixApiKey,
                request,
                ResponseDto.class
        );
        if (response == null || response.distance.size() != addresses.size()) {
            throw new OnlineShopInternalException("Invalid response: " + response);
        }
        return response.distance;

    }

    private String addressToString(Address address) {
        return address.getStreetAddress() + "," + address.getCity() + "," + address.getCountry();
    }

    private record RequestDto(
            List<String> locations,
            boolean manyToOne
    ) {

    }

    private record ResponseDto(
            List<Long> distance,
            List<Long> time
    ) {

    }
}
