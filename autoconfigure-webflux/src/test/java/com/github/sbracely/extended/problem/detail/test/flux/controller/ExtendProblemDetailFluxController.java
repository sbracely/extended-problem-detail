package com.github.sbracely.extended.problem.detail.test.flux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/extend-problem-detail-flux")
public class ExtendProblemDetailFluxController {

    @GetMapping("/method-not-allowed")
    public void methodNotAllowed() {
        log.info("method-not-allowed");
    }

    @GetMapping(path = "/not-acceptable-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public void notAcceptableStatus() {
        log.info("not-acceptable-status");
    }

    @PostMapping(path = "/unsupported-media-type", consumes = MediaType.APPLICATION_XML_VALUE)
    public void unsupportedMediaType() {
        log.info("unsupported media type");
    }

    @GetMapping("/missing-request-value")
    public void missingRequestValue(@RequestParam String id) {
        log.info("missing-request-value id: {}", id);
    }
}
