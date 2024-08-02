package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {
    @Autowired
    UserService usrService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public String info()
    {
        log.info("Controller info!!!");
        return "hello";
    }

    @PostMapping("/join")
    public void join(@RequestBody UserDto usr)
    {
        log.info("Controller join!!!");
        usr.setPassword(passwordEncoder.encode(usr.getPassword()));
        usrService.CreateUser(usr.toEntity());
    }
}
