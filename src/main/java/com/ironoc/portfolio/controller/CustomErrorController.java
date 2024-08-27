package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public RedirectView error(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    	log.error("Bad request - redirect to home");
        return new RedirectView("/", false);
    }

    public String getErrorPath() {
        return PATH;
    }

}
