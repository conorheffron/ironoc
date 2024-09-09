package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.slf4j.Log4jLogger;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Enumeration;

@Controller
@Slf4j
public class HomeController {

	@RequestMapping( {"/"} )
	public String index(HttpServletRequest request) {
		log.info("Home page request details, host={}, uri={}, user-agent={}",
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		return "index";
	}
}
