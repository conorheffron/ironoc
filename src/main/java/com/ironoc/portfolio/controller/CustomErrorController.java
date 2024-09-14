package com.ironoc.portfolio.controller;

import com.ironoc.portfolio.logger.AbstractLogger;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class CustomErrorController extends AbstractLogger implements  ErrorController {

    protected static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public RedirectView error(HttpServletRequest request) {
        error("Unexpected error occurred. {}, The HTTP status is: {}",
                request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        RedirectView redirectView = new RedirectView("/", false);
        error("Bad request for {}. Redirecting to home",
                request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        return redirectView;
    }
}
