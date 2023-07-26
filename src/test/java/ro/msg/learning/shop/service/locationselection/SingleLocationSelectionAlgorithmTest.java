package ro.msg.learning.shop.service.locationselection;

import org.junit.jupiter.api.Test;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.exception.LocationSelectionException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SingleLocationSelectionAlgorithmTest {

    private final SingleLocationSelectionAlgorithm algorithm = new SingleLocationSelectionAlgorithm();

    @Test
    void testSelectLocationForItems_singleProductSingleSolution_selectSolution() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1
                        ))
                        .build()
        );
        var result = algorithm.selectLocationForItems(items);
        assertThat(result)
                .singleElement()
                .hasFieldOrPropertyWithValue("product", DummyData.product1)
                .extracting("shippedFrom")
                .isEqualTo(location1);
    }

    @Test
    void testSelectLocationForItems_singleProductMultipleSolutions_selectSolutionWithLowestID() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);
        final var location2 = DummyData.createLocation(2, 20, DummyData.products);

        location1.setId(UUID.fromString("db798b30-54fd-44c8-a2fd-ad6690fdc4be"));
        location2.setId(UUID.fromString("a6fa4556-b305-4407-86d5-0ad99790d287"));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1,
                                location2
                        ))
                        .build()
        );
        var result = algorithm.selectLocationForItems(items);
        assertThat(result)
                .singleElement()
                .hasFieldOrPropertyWithValue("product", DummyData.product1)
                .extracting("shippedFrom")
                .isEqualTo(location2);
    }

    @Test
    void testSelectLocationForItems_multipleProductsSingleSolution_selectSolution() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);
        final var location2 = DummyData.createLocation(2, 20, List.of(DummyData.product1));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1,
                                location2
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product2)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1
                        ))
                        .build()
        );
        var result = algorithm.selectLocationForItems(items);
        assertThat(result)
                .hasSize(2)
                .allMatch(orderDetail -> orderDetail.getShippedFrom().equals(location1));
    }

    @Test
    void testSelectLocationForItems_multipleProductsMultipleSolution_selectSolutionWithLowestID() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);
        final var location2 = DummyData.createLocation(2, 20, DummyData.products);

        location1.setId(UUID.fromString("db798b30-54fd-44c8-a2fd-ad6690fdc4be"));
        location2.setId(UUID.fromString("a6fa4556-b305-4407-86d5-0ad99790d287"));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1,
                                location2
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product2)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1,
                                location2
                        ))
                        .build()
        );
        var result = algorithm.selectLocationForItems(items);
        assertThat(result)
                .hasSize(2)
                .allMatch(orderDetail -> orderDetail.getShippedFrom().equals(location2));
    }

    @Test
    void testSelectLocationForItems_singleProductNoSolution_fail() {
        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                        ))
                        .build()
        );
        assertThatThrownBy(() -> algorithm.selectLocationForItems(items))
                .isInstanceOf(LocationSelectionException.class);
    }

    @Test
    void testSelectLocationForItems_multipleProductsNoSolution_fail() {
        final var location1 = DummyData.createLocation(1, 20, List.of(DummyData.product2));
        final var location2 = DummyData.createLocation(2, 20, List.of(DummyData.product1));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location2
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product2)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1
                        ))
                        .build()
        );
        assertThatThrownBy(() -> algorithm.selectLocationForItems(items))
                .isInstanceOf(LocationSelectionException.class);
    }

}