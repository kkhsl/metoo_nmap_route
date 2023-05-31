package com.metoo.nspm.core.manager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/web/login")
@Controller
public class LoginControllerWeb {

    @GetMapping("/exec")
    public String login(){
        return "login";
    }
}
