package com.github.hoyoung.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by HoYoung on 2021/02/18.
 */
@Controller
public class LoginController {

  @GetMapping("/login")
  public String login(Model model){
    model.addAttribute("name", "aaa");
    return "login";
  }

  @GetMapping("/main")
  public String index(Model model){
    model.addAttribute("name", "aaa");
    return "index";
  }

  @GetMapping("/books")
  public String books(Model model){
    model.addAttribute("name", "aaa");
    return "index";
  }

}
