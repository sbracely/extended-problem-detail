package io.github.sbracely.extended.problem.detail.webmvc.example.controller;

import io.github.sbracely.extended.problem.detail.common.response.ExtendedProblemDetail;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.CONTENT_TOO_LARGE;

@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcControllerRandomPortTests {

    private static final Logger logger = LoggerFactory.getLogger(MvcControllerRandomPortTests.class);
    private static final String BASE_PATH = "/mvc-extended-problem-detail";

    @LocalServerPort
    private int port;

    /**
     * @see AsyncRequestNotUsableException
     * @see MvcProblemDetailController#asyncRequestNotUsableException()
     */
    @Test
    void asyncRequestNotUsableException() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMillis(1));

        RestClient restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl("http://localhost:" + port)
                .build();
        assertThatThrownBy(() -> restClient.get()
                .uri(BASE_PATH + "/async-request-not-usable-exception")
                .retrieve()
                .toBodilessEntity())
                .isExactlyInstanceOf(ResourceAccessException.class)
                .hasCauseInstanceOf(SocketTimeoutException.class)
                .hasMessageContaining("Read timed out");
    }

    /**
     * @see MaxUploadSizeExceededException
     */
    @Nested
    @TestPropertySource(properties = "spring.servlet.multipart.max-file-size=1")
    class MvcMaxUploadSizeExceededExceptionTests {

        @Autowired
        private TestRestTemplate testRestTemplate;

        /**
         * @see MaxUploadSizeExceededException
         * @see MvcProblemDetailController#maxUploadSizeExceededException(MultipartFile)
         */
        @Test
        void maxUploadSizeExceededException() {
            ResponseEntity<ExtendedProblemDetail> response = uploadOversizedFile(List.of(Locale.ENGLISH));

            assertThat(response.getStatusCode()).isEqualTo(CONTENT_TOO_LARGE);
            assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
            ExtendedProblemDetail extendedProblemDetail = response.getBody();
            logger.info("extendedProblemDetail: {}", extendedProblemDetail);
            assertThat(extendedProblemDetail).isNotNull();
            assertThat(extendedProblemDetail.getType()).isEqualTo(URI.create("about:blank"));
            assertThat(extendedProblemDetail.getTitle()).isEqualTo(CONTENT_TOO_LARGE.getReasonPhrase());
            assertThat(extendedProblemDetail.getStatus()).isEqualTo(CONTENT_TOO_LARGE.value());
            assertThat(extendedProblemDetail.getDetail()).isEqualTo("Maximum upload size exceeded");
            assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(BASE_PATH + "/max-upload-size-exceeded-exception"));
            assertThat(extendedProblemDetail.getProperties()).isNull();
            assertThat(extendedProblemDetail.getErrors()).isNull();
        }

        @Test
        void maxUploadSizeExceededExceptionLocalized() {
            ResponseEntity<ExtendedProblemDetail> response = uploadOversizedFile(List.of(Locale.SIMPLIFIED_CHINESE));

            ExtendedProblemDetail extendedProblemDetail = response.getBody();
            assertThat(extendedProblemDetail).isNotNull();
            assertThat(extendedProblemDetail.getType()).isEqualTo(URI.create("about:blank"));
            assertThat(extendedProblemDetail.getTitle()).isEqualTo("内容过大");
            assertThat(extendedProblemDetail.getDetail()).isEqualTo("超过最大上传大小");
        }

        private ResponseEntity<ExtendedProblemDetail> uploadOversizedFile(List<Locale> locales) {
            byte[] largeContent = new byte[2];

            ByteArrayResource resource = new ByteArrayResource(largeContent) {
                @Override
                public String getFilename() {
                    return "large-test-file.txt";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAcceptLanguageAsLocales(locales);

            String uri = BASE_PATH + "/max-upload-size-exceeded-exception";
            return testRestTemplate.postForEntity(
                    "http://localhost:" + port + uri,
                    new HttpEntity<>(body, headers),
                    ExtendedProblemDetail.class
            );
        }
    }
}
