package ro.msg.learning.shop.service.locationselection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.msg.learning.shop.exception.LocationSelectionException;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ro.msg.learning.shop.DummyData.addressMSGBrassai;
import static ro.msg.learning.shop.DummyData.coloredPencils;
import static ro.msg.learning.shop.DummyData.createStocks;
import static ro.msg.learning.shop.DummyData.johnSmith;
import static ro.msg.learning.shop.DummyData.theJungleBook;

class SingleLocationSelectionAlgorithmTest {

    public static final String UUID_LOWEST = "a6fa4556-b305-4407-86d5-0ad99790d287";
    public static final String UUID_HIGHEST = "db798b30-54fd-44c8-a2fd-ad6690fdc4be";
    private final SingleLocationSelectionAlgorithm algorithm = new SingleLocationSelectionAlgorithm();
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
    void testSelectLocationForItems_singleProductMultipleSolutions_selectSolutionWithLowestID() {
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook));
        bucurestiWarehouse.setId(UUID.fromString(UUID_LOWEST));
        clujWarehouse.setStocks(createStocks(20, theJungleBook));
        clujWarehouse.setId(UUID.fromString(UUID_HIGHEST));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse,
                                bucurestiWarehouse
                        ))
                        .build()
        );
        final var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .singleElement()
                .hasFieldOrPropertyWithValue("product", theJungleBook)
                .extracting("shippedFrom")
                .isEqualTo(bucurestiWarehouse);
    }

    @Test
    void testSelectLocationForItems_multipleProductsSingleSolution_selectSolution() {
        clujWarehouse.setStocks(createStocks(20, theJungleBook, coloredPencils));
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse,
                                bucurestiWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse
                        ))
                        .build()
        );
        final var result = runLocationSelectionAlgorithm(items);
        assertThat(result)
                .hasSize(2)
                .allMatch(orderDetail -> clujWarehouse.equals(orderDetail.getShippedFrom()));
    }

    @Test
    void testSelectLocationForItems_multipleProductsMultipleSolution_selectSolutionWithLowestID() {
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook));
        bucurestiWarehouse.setId(UUID.fromString(UUID_LOWEST));
        clujWarehouse.setStocks(createStocks(20, theJungleBook));
        clujWarehouse.setId(UUID.fromString(UUID_HIGHEST));

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
                .allMatch(orderDetail -> orderDetail.getShippedFrom().equals(bucurestiWarehouse));
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
        bucurestiWarehouse.setStocks(createStocks(20, theJungleBook));
        clujWarehouse.setStocks(createStocks(20, coloredPencils));

        final var items = Set.of(
                OrderDetailWithPotentialLocations.builder()
                        .product(theJungleBook)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                bucurestiWarehouse
                        ))
                        .build(),
                OrderDetailWithPotentialLocations.builder()
                        .product(coloredPencils)
                        .quantity(1)
                        .potentialLocations(Set.of(
                                clujWarehouse
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