package org.example.exceptionhandlerexample.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlerexample.reuqest.user.UserRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/hello")
    public Object hello(@Validated UserRequest userRequest) {
        log.info("userRequest={}", userRequest);
        return userRequest;
    }

    @GetMapping("/echo")
    public String echo(@NotBlank(message = "echo不能为空") @NotNull(message = "echo不能为null") String echo) {
        log.info("echo={}", echo);
        return echo;
    }
}
