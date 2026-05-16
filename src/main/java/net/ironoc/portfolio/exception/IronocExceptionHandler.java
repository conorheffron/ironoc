package net.ironoc.portfolio.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IronocExceptionHandler {

    @ExceptionHandler(IronocJsonException.class)
    public ProblemDetail handleIronocJsonException(IronocJsonException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
        problemDetail.setTitle("JSON processing failed");
        problemDetail.setProperty("path", request.getRequestURI());
        return problemDetail;
    }
}
