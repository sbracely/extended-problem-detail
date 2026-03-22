package org.example.exceptionhandlerexample.controller.flux;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlerexample.reuqest.problem.ProblemDetailRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/flux-problem-detail")
public class FluxProblemDetailController {
    @PostMapping("/create")
    public Mono<Void> create(@RequestBody ProblemDetailRequest problemDetailRequest) {
        log.info("problemRequest: {}", problemDetailRequest);
        return Mono.empty();
    }
}
