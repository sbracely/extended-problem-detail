package com.github.sbrace.nested.problem.detail.response;

import org.springframework.http.ProblemDetail;

import java.util.List;

public class NestedProblemDetail extends ProblemDetail {

    private String errorCode;

    private List<Error> errors;

    public NestedProblemDetail() {
    }

    public NestedProblemDetail(ProblemDetail problemDetail) {
        super(problemDetail);
        this.errorCode = ErrorCode.httpStatusValue(problemDetail.getStatus());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    protected String initToStringContent() {
        return super.initToStringContent() +
                ", errorCode='" + errorCode + "'" +
                ", errors=" + errors;
    }
}
