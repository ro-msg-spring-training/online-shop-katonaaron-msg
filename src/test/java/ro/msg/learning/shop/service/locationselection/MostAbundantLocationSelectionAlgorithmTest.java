package ro.msg.learning.shop.service.locationselection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ro.msg.learning.shop.test.DummyData.addressMSGBrassai;
import static ro.msg.learning.shop.test.DummyData.coloredPencils;
import static ro.msg.learning.shop.test.DummyData.createStocks;
import static ro.msg.learning.shop.test.DummyData.johnSmith;
import static ro.msg.learning.shop.test.DummyData.theJungleBook;

class MostAbundantLocationSelectionAlgorithmTest {
    private final MostAbundantLocationSelectionAlgorithm algorithm = new MostAbundantLocationSelectionAlgorithm();

    private Location clujWarehouse;
    private Location bucurestiWarehouse;

    @BeforeEach
    public void setUp() {
        bucurestiWarehouse = new Location(
                "Bucuresti Warehouse",
                new Address("Romania", "Bucuresti", "Bucuresti", "str. Fabricii 2"),
                emptySet()
        );
        clujWarehouse = new Location(
                "Cluj-Napoca Warehouse",
                new Address("Romania", "Cluj-Napoca", "Cluj", "str. Fabricii 2"),
                emptySet()
        );
    }

    @Test
    void testSelectLocationForItems_singleProductSingleSolution_selectSolution() {
        clujWarehouse.setStocks(createStocks(20, theJungleBook));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse
                        ))
                        .build()
        );
        final var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .singleElement()
                .hasFieldOrPropertyWithValue("product", theJungleBook)
                .extracting("shippedFrom")
                .isEqualTo(clujWarehouse);
    }

    @Test
    void testSelectLocationForItems_singleProductMultipleSolutions_selectSolutionWithTheLargestQuantity() {
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook));
        clujWarehouse.setStocks(createStocks(40, theJungleBook));


        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse,
                                clujWarehouse
                        ))
                        .build()
        );
        var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .singleElement()
                .hasFieldOrPropertyWithValue("product", theJungleBook)
                .extracting("shippedFrom")
                .isEqualTo(clujWarehouse);
    }

    @Test
    void testSelectLocationForItems_multipleProductsSameSolution_selectSolution() {
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook, coloredPencils));
        clujWarehouse.setStocks(createStocks(10, theJungleBook));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse,
                                clujWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse
                        ))
                        .build()
        );
        var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .hasSize(2)
                .allMatch(orderDetail -> bucurestiWarehouse.equals(orderDetail.getShippedFrom()));
    }

    @Test
    void testSelectLocationForItems_multipleProductsIndependentSolutions_selectSolutionsIndependently() {
        bucurestiWarehouse.setStocks(createStocks(20, coloredPencils));
        clujWarehouse.setStocks(createStocks(20, theJungleBook));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse
                        ))
                        .build()
        );
        var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .hasSize(2)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(theJungleBook))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(clujWarehouse);

        assertThat(result)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(coloredPencils))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(bucurestiWarehouse);
    }

    @Test
    void testSelectLocationForItems_multipleProductsMultipleSolution_selectSolutionWithTheLargestQuantityForEachProduct() {
        bucurestiWarehouse.setStocks(createStocks(Map.of(
                theJungleBook, 20,
                coloredPencils, 10
        )));
        clujWarehouse.setStocks(createStocks(Map.of(
                theJungleBook, 10,
                coloredPencils, 20
        )));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse,
                                clujWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse,
                                clujWarehouse
                        ))
                        .build()
        );
        final var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .hasSize(2)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(theJungleBook))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(bucurestiWarehouse);

        assertThat(result)
                .filteredOn(orderDetail -> orderDetail.getProduct().equals(coloredPencils))
                .singleElement()
                .extracting("shippedFrom")
                .isEqualTo(clujWarehouse);
    }


    @Test
    void testSelectLocationForItems_singleProductNoSolution_fail() {
        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                        ))
                        .build()
        );
        assertThatThrownBy(() -> runLocationSelectionAlgorithm(items))
                .isInstanceOf(LocationSelectionException.class);
    }

    @Test
    void testSelectLocationForItems_multipleProductsNoSolution_fail() {
        bucurestiWarehouse.setStocks(createStocks(20, coloredPencils));
        clujWarehouse.setStocks(createStocks(20, theJungleBook));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(100)
                        .potentialLocations(Set.of(
                                clujWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(100)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse
                        ))
                        .build()
        );
        assertThatThrownBy(() -> runLocationSelectionAlgorithm(items))
                .isInstanceOf(LocationSelectionException.class);
    }

    private Set<OrderDetail> runLocationSelectionAlgorithm(Set<OrderDetailWithPotentialLocations> items) {
        final var order = new OrderWithPotentialLocations(
                johnSmith,
                LocalDateTime.now(),
                addressMSGBrassai,
                items);

        return algorithm.selectLocationForItems(order);
    }
}