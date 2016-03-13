package sk.loffay.wandera.exception;

/**
 * @author Pavol Loffay
 */
public class WanderaNotificationException extends RuntimeException {

    public WanderaNotificationException() {
    }

    public WanderaNotificationException(String message) {
        super(message);
    }

    public WanderaNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WanderaNotificationException(Throwable cause) {
        super(cause);
    }

    public WanderaNotificationException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
