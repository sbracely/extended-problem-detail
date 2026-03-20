package org.example.exceptionhandlerexample.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlerexample.reuqest.problem.ProblemDetailRequest;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TestController {


    @GetMapping("/matrix/{matrixVariable}")
    public String matrix(@MatrixVariable List<@NotBlank String> matrixVariable) {
        return "matrix";
    }

    @PostMapping("/hello")
    public Object hello(@Validated ProblemDetailRequest problemDetailRequest) {
        log.info("problemRequest = {}", problemDetailRequest);
        return problemDetailRequest;
    }

    @GetMapping(value = "/echo")
    public String echo(@NotBlank String file) {
        log.info("file = {}", file);
        return file;
    }

    @PostMapping("/list")
    public List<ProblemDetailRequest> list(@Valid List<ProblemDetailRequest> problemDetailRequestList) {
        log.info("problemRequestList = {}", problemDetailRequestList);
        return problemDetailRequestList;
    }

    @GetMapping(value = "/cookie")
    public String cookieValue(@CookieValue
                              @NotNull(message = "不能是 null")
                              @NotBlank(message = "不能空白")
                              @Length(min = 2, message = "长度最小是 2")
                              String cookieValue) {
        log.info("cookieValue={}", cookieValue);
        return cookieValue;
    }
}
