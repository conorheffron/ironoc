package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

	@RequestMapping( {"/"} )
	public String index(HttpServletRequest httpServletRequest) {
		log.info(httpServletRequest.getRequestURI() + " page request");
		return "index";
	}
}
