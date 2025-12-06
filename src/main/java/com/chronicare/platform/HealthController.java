package com.chronicare.platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("rootController")
public class HealthController {

    @GetMapping("/")
    public String root() {
        return "ChroniCare Backend is running!";
    }
}
