package com.jacky.strive.oauth2.controller;

import com.jacky.strive.oauth2.config.CustomUserService;
import com.jacky.strive.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    OauthService oauthService;

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