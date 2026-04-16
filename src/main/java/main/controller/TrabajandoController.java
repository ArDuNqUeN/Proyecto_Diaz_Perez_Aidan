package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrabajandoController {

    @GetMapping("/trabajando")
    public String login() {
        return "trabajando";
    }
}