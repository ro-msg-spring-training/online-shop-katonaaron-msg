package ro.msg.learning.shop.exception;

import lombok.Getter;

@Getter
public class LocationOutOfStockException extends RuntimeException {
    public LocationOutOfStockException() {
    }

    public LocationOutOfStockException(String message) {
        super(message);
    }

    public LocationOutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationOutOfStockException(Throwable cause) {
        super(cause);
    }
}
