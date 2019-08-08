package edu.pkch.mvcedu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller("/users")
public class UserController {

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> hello() {
        return ResponseEntity.of(Optional.of("hello world!"));
    }
}
