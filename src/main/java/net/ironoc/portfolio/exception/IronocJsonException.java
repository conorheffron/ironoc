package net.ironoc.portfolio.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class IronocJsonException extends JsonProcessingException {

    public IronocJsonException(String message) {
        super(message);
    }

    public IronocJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
