package com.me.demo4.web.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@Controller
@RequestMapping("firstController")
public class FirstController {
    @RequestMapping("/first.do")
    @ResponseBody
    public String first(){
        return "hello word";
    }
}
