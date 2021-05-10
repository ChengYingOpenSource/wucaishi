package com.cy.onepush.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page
 *
 * @author WhatAKitty
 * @date 2021-5-7
 * @since 0.1.0
 */
@Controller
public class HomeController {

    @RequestMapping(value = {"", "/", "/home"})
    public String index() {
        return "index";
    }

}
