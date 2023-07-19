package ro.msg.learning.shop.exception;

public class OnlineShopException extends RuntimeException {
    public OnlineShopException() {
    }

    public OnlineShopException(String message) {
        super(message);
    }

    public OnlineShopException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlineShopException(Throwable cause) {
        super(cause);
    }
}
