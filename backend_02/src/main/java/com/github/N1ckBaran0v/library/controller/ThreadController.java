package com.github.N1ckBaran0v.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread")
public class ThreadController {
    @GetMapping("/watch")
    public String thread() {
        return Thread.currentThread().toString();
    }
}
