package com.ironoc.portfolio.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
public class CustomErrorController implements ErrorController {

    protected static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public RedirectView error(HttpServletRequest request) {
        log.error("Unexpected error occurred. {}, The HTTP status is: {}",
                request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        RedirectView redirectView = new RedirectView("/", false);
        log.error("Bad request for {}. Redirecting to home",
                request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        return redirectView;
    }
}
