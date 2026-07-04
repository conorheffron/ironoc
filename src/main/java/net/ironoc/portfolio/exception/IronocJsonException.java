package net.ironoc.portfolio.exception;

<<<<<<< HEAD
import com.fasterxml.jackson.core.JsonProcessingException;

public class IronocJsonException extends JsonProcessingException {
=======
public class IronocJsonException extends RuntimeException {
>>>>>>> origin/main

    public IronocJsonException(String message) {
        super(message);
    }

    public IronocJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
