package com.jacky.strive.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class WebController {

	// @PreAuthorize("#oauth2.hasScope('read')")
	@RequestMapping("/")
	public String home() {
		return "home";
	}

	@RequestMapping("/home")
	public String index(Principal principal, Model model) {
		if (principal == null) {
			return "home";
		}

		model.addAttribute("principal", principal);
		return "home";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}