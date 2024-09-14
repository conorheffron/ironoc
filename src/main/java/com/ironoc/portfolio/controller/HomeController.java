package com.ironoc.portfolio.controller;

import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.service.GitDetails;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends AbstractLogger {

	private final GitDetails gitDetails;

	@Autowired
	public HomeController(GitDetails gitDetails) {
		this.gitDetails = gitDetails;
	}

	@RequestMapping( {"/"} )
	public String index(HttpServletRequest request) {
		info("Home page request details, host={}, uri={}, user-agent={}",
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		return "index";
	}
}
