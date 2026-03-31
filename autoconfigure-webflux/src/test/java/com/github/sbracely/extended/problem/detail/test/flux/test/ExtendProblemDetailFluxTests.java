package com.github.sbracely.extended.problem.detail.test.flux.test;

import com.github.sbracely.extended.problem.detail.response.ExtendedProblemDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ALLOW;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "PT10M")
class ExtendProblemDetailFluxTests {

    @Autowired
    private WebTestClient webTestClient;

    private static final String BASE_PATH = "/extend-problem-detail-flux";

    @Test
    void methodNotAllowedException() {
        String uri = BASE_PATH + "/method-not-allowed";
        ExtendedProblemDetail extendedProblemDetail = webTestClient.delete().uri(uri).exchange()
                .expectStatus().isEqualTo(METHOD_NOT_ALLOWED)
                .expectHeader().valueEquals(ALLOW, GET.name())
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ExtendedProblemDetail.class)
                .returnResult().getResponseBody();
        log.info("extendedProblemDetail: {}", extendedProblemDetail);
        assertThat(extendedProblemDetail).isNotNull();
        assertThat(extendedProblemDetail.getType()).isNull();
        assertThat(extendedProblemDetail.getTitle()).isEqualTo(METHOD_NOT_ALLOWED.getReasonPhrase());
        assertThat(extendedProblemDetail.getStatus()).isEqualTo(METHOD_NOT_ALLOWED.value());
        assertThat(extendedProblemDetail.getDetail()).isEqualTo("Supported methods: [GET]");
        assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(uri));
        assertThat(extendedProblemDetail.getProperties()).isNull();
        assertThat(extendedProblemDetail.getErrors()).isNull();
    }

    @Test
    void notAcceptableStatusException() {
        String uri = BASE_PATH + "/not-acceptable-status";
        ExtendedProblemDetail extendedProblemDetail = webTestClient.get().uri(uri)
                .header(ACCEPT, APPLICATION_XML_VALUE)
                .exchange()
                .expectStatus().isEqualTo(NOT_ACCEPTABLE)
                .expectHeader().valueEquals(ACCEPT, APPLICATION_JSON_VALUE)
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ExtendedProblemDetail.class)
                .returnResult().getResponseBody();
        log.info("extendedProblemDetail: {}", extendedProblemDetail);
        assertThat(extendedProblemDetail).isNotNull();
        assertThat(extendedProblemDetail.getType()).isNull();
        assertThat(extendedProblemDetail.getTitle()).isEqualTo(NOT_ACCEPTABLE.getReasonPhrase());
        assertThat(extendedProblemDetail.getStatus()).isEqualTo(NOT_ACCEPTABLE.value());
        assertThat(extendedProblemDetail.getDetail()).isEqualTo("Acceptable representations: [application/json].");
        assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(uri));
        assertThat(extendedProblemDetail.getProperties()).isNull();
        assertThat(extendedProblemDetail.getErrors()).isNull();
    }

    @Test
    void unsupportedMediaTypeStatusException() {
        String uri = BASE_PATH + "/unsupported-media-type";
        ExtendedProblemDetail extendedProblemDetail = webTestClient.post().uri(uri).exchange()
                .expectStatus().isEqualTo(UNSUPPORTED_MEDIA_TYPE)
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectHeader().valueEquals(ACCEPT, APPLICATION_XML_VALUE)
                .expectBody(ExtendedProblemDetail.class)
                .returnResult().getResponseBody();
        log.info("extendedProblemDetail: {}", extendedProblemDetail);
        assertThat(extendedProblemDetail).isNotNull();
        assertThat(extendedProblemDetail.getType()).isNull();
        assertThat(extendedProblemDetail.getTitle()).isEqualTo(UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());
        assertThat(extendedProblemDetail.getStatus()).isEqualTo(UNSUPPORTED_MEDIA_TYPE.value());
        assertThat(extendedProblemDetail.getDetail()).isNull();
        assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(uri));
        assertThat(extendedProblemDetail.getProperties()).isNull();
        assertThat(extendedProblemDetail.getErrors()).isNull();
    }

    @Test
    void missingRequestValueException() {
        String uri = BASE_PATH + "/missing-request-value";
        ExtendedProblemDetail extendedProblemDetail = webTestClient.get().uri(uri).exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ExtendedProblemDetail.class)
                .returnResult().getResponseBody();
        log.info("extendedProblemDetail: {}", extendedProblemDetail);
        assertThat(extendedProblemDetail).isNotNull();
        assertThat(extendedProblemDetail.getType()).isNull();
        assertThat(extendedProblemDetail.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
        assertThat(extendedProblemDetail.getStatus()).isEqualTo(BAD_REQUEST.value());
        assertThat(extendedProblemDetail.getDetail()).isEqualTo("Required query parameter 'id' is not present.");
        assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(uri));
        assertThat(extendedProblemDetail.getProperties()).isNull();
        assertThat(extendedProblemDetail.getErrors()).isNull();
    }
}
