package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class CustomErrorController implements ErrorController {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);
	
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public RedirectView error(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    	LOGGER.error("Bad request - redirect to home");
        return new RedirectView("/", false);
    }

    public String getErrorPath() {
        return PATH;
    }

}
