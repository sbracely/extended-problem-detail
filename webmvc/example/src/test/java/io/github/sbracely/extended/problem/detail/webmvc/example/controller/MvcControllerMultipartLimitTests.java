package io.github.sbracely.extended.problem.detail.webmvc.example.controller;

import io.github.sbracely.extended.problem.detail.common.response.ExtendedProblemDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.servlet.multipart.max-file-size=1")
class MvcControllerMultipartLimitTests {

    private static final String BASE_PATH = "/mvc-extended-problem-detail";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * @see MaxUploadSizeExceededException
     * @see MvcProblemDetailController#maxUploadSizeExceededException(org.springframework.web.multipart.MultipartFile)
     */
    @Test
    void maxUploadSizeExceededException() {
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

        String uri = BASE_PATH + "/max-upload-size-exceeded-exception";
        ResponseEntity<ExtendedProblemDetail> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + uri,
                new HttpEntity<>(body, headers),
                ExtendedProblemDetail.class);

        assertThat(response.getStatusCode().value()).isEqualTo(413);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);

        ExtendedProblemDetail extendedProblemDetail = response.getBody();
        assertThat(extendedProblemDetail).isNotNull();
        assertThat(extendedProblemDetail.getType()).isEqualTo(URI.create("about:blank"));
        assertThat(extendedProblemDetail.getTitle()).isEqualTo("Payload Too Large");
        assertThat(extendedProblemDetail.getStatus()).isEqualTo(413);
        assertThat(extendedProblemDetail.getDetail()).isEqualTo("Maximum upload size exceeded");
        assertThat(extendedProblemDetail.getInstance()).isEqualTo(URI.create(uri));
        assertThat(extendedProblemDetail.getProperties()).isNull();
        assertThat(extendedProblemDetail.getErrors()).isNull();
    }
}
