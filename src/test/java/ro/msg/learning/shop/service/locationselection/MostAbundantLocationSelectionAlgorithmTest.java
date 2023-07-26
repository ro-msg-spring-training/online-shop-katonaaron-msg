package ro.msg.learning.shop.service.locationselection;

import org.junit.jupiter.api.Test;
import ro.msg.learning.shop.DummyData;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Stock;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MostAbundantLocationSelectionAlgorithmTest {
    private final MostAbundantLocationSelectionAlgorithm algorithm = new MostAbundantLocationSelectionAlgorithm();

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
    void testSelectLocationForItems_singleProductMultipleSolutions_selectSolutionWithTheLargestQuantity() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);
        final var location2 = DummyData.createLocation(2, 40, DummyData.products);

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
    void testSelectLocationForItems_multipleProductsSameSolution_selectSolution() {
        final var location1 = DummyData.createLocation(1, 20, DummyData.products);
        final var location2 = DummyData.createLocation(2, 10, List.of(DummyData.product1));

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
    void testSelectLocationForItems_multipleProductsIndependentSolutions_selectSolutionsIndependently() {
        final var location1 = DummyData.createLocation(1, 20, List.of(DummyData.product1));
        final var location2 = DummyData.createLocation(2, 20, List.of(DummyData.product2));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location1
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product2)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                location2
                        ))
                        .build()
        );
        var result = algorithm.selectLocationForItems(items);
        assertThat(result)
                .hasSize(2)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(DummyData.product1))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(location1);

        assertThat(result)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(DummyData.product2))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(location2);
    }

    @Test
    void testSelectLocationForItems_multipleProductsMultipleSolution_selectSolutionWithTheLargestQuantityForEachProduct() {
        final var location1 = DummyData.createLocation(1, List.of(
                new Stock(DummyData.product1, 20),
                new Stock(DummyData.product2, 10)
        ));
        final var location2 = DummyData.createLocation(2, List.of(
                new Stock(DummyData.product1, 10),
                new Stock(DummyData.product2, 20)
        ));

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
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(DummyData.product1))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(location1);

        assertThat(result)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(DummyData.product2))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(location2);
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
        final var location1 = DummyData.createLocation(1, 20, List.of(DummyData.product1));
        final var location2 = DummyData.createLocation(2, 20, List.of(DummyData.product2));

        final var items = List.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product1)
                        .quantity(100)
                        .potentialLocations(Set.of(
                                location1
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(DummyData.product2)
                        .quantity(100)
                        .potentialLocations(Set.of(
                                location2
                        ))
                        .build()
        );
        assertThatThrownBy(() -> algorithm.selectLocationForItems(items))
                .isInstanceOf(LocationSelectionException.class);
    }

}