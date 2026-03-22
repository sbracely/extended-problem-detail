package org.example.exceptionhandlerexample.test.controller.flux;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.exceptionhandlerexample.controller.flux.FluxProblemDetailController;
import org.example.exceptionhandlerexample.response.NestedProblemDetail;
import org.example.exceptionhandlerexample.reuqest.problem.ProblemDetailRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@WebFluxTest(FluxProblemDetailController.class)
class ProblemDetailControllerTests {


    @Autowired
    private WebTestClient webTestClient;

    private static final String BASE_PATH = "/flux-problem-detail";

    @Disabled
    @Nested
    @TestPropertySource(properties = "spring.http.codecs.max-in-memory-size=1")
    class ContentTooLargeException {
        @Test
        void contentTooLargeException() {
            String uri = BASE_PATH + "/create";
            ProblemDetailRequest problemDetailRequest = new ProblemDetailRequest();
            problemDetailRequest.setName("abc");
            problemDetailRequest.setPassword("123");
            WebTestClient.ResponseSpec result = webTestClient.post()
                    .uri(uri)
                    .contentType(APPLICATION_JSON)
                    .body(Mono.just(problemDetailRequest), ProblemDetailRequest.class)
                    .exchange();
            Assertions.assertThat(result).isNotNull();
            NestedProblemDetail responseBody = result.expectStatus()
                    .isEqualTo(HttpStatusCode.valueOf(413))
                    .expectBody(NestedProblemDetail.class)
                    .returnResult()
                    .getResponseBody();
            log.info("responseBody: {}", responseBody);
        }
    }
}
