package ro.msg.learning.shop.exception;

public class OnlineShopInternalException extends RuntimeException {
    public OnlineShopInternalException() {
    }

    public OnlineShopInternalException(String message) {
        super(message);
    }

    public OnlineShopInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlineShopInternalException(Throwable cause) {
        super(cause);
    }
}
