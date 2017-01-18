package com.ironoc.portfolio.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping( {"/"} )
	public String index(HttpServletRequest httpServletRequest) {
		LOGGER.info(httpServletRequest.getRequestURI() + " page request");
		return "index";
	}

}
