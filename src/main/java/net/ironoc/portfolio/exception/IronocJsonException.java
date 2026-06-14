package net.ironoc.portfolio.exception;

public class IronocJsonException extends RuntimeException {

    public IronocJsonException(String message) {
        super(message);
    }

    public IronocJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
