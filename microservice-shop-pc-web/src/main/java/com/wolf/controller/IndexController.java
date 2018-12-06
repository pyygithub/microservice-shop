package com.wolf.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private static final String INDEX_HOME = "index";

    /**
     * 主页
     */
    @GetMapping("/")
    public String index() {
        return INDEX_HOME;
    }
}