package com.github.sbracely.extended.problem.detail.test.mvc.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class BusinessException extends ErrorResponseException {

    public BusinessException(HttpStatusCode status, ProblemDetail body) {
        super(status, body, null);
    }

    public BusinessException(HttpStatusCode status, ProblemDetail body, Throwable cause) {
        super(status, body, cause);
    }
}
