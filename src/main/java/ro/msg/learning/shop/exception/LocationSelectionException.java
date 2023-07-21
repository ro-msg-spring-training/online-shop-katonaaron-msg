package ro.msg.learning.shop.exception;

public class LocationSelectionException extends OnlineShopException {
    public LocationSelectionException(String message) {
        super(message);
    }

    public LocationSelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
