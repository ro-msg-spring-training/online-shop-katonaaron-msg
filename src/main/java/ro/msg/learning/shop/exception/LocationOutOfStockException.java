package ro.msg.learning.shop.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LocationOutOfStockException extends RuntimeException {
    private final UUID locationId;
    private final UUID productId;
    private final int quantity;

    public LocationOutOfStockException(UUID locationId, UUID productId, int quantity) {
        super("Location \"" + locationId
                + "\" does not have in stock product \"" + productId
                + "\" with quantity " + quantity);
        this.locationId = locationId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
