package com.github.sbracely.extended.problem.detail.test.flux.controller;

import com.github.sbracely.extended.problem.detail.response.Error;
import com.github.sbracely.extended.problem.detail.response.ExtendedProblemDetail;
import com.github.sbracely.extended.problem.detail.test.flux.exception.ExtendedErrorResponseException;
import com.github.sbracely.extended.problem.detail.test.flux.reuqest.ProblemDetailRequest;
import com.github.sbracely.extended.problem.detail.test.flux.reuqest.valid.annocation.CheckFilePart;
import com.github.sbracely.extended.problem.detail.test.flux.reuqest.valid.annocation.CheckPassword;
import com.github.sbracely.extended.problem.detail.test.flux.service.ProblemDetailService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.accept.MissingApiVersionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/flux-extended-problem-detail")
public class FluxExtendedProblemDetailController {

    private final ProblemDetailService problemDetailService;

    public FluxExtendedProblemDetailController(ProblemDetailService problemDetailService) {
        this.problemDetailService = problemDetailService;
    }

    /**
     * Handle method not allowed exception.
     *
     * @see MethodNotAllowedException
     */
    @GetMapping("/method-not-allowed-exception")
    public Mono<Void> methodNotAllowedException() {
        log.info("method-not-allowed");
        return Mono.empty();
    }

    /**
     * Handle not acceptable status exception.
     *
     * @see NotAcceptableStatusException
     */
    @GetMapping(path = "/not-acceptable-status-exception", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> notAcceptableStatusException() {
        log.info("not-acceptable-status");
        return Mono.empty();
    }

    /**
     * Handle unsupported media type status exception.
     *
     * @see UnsupportedMediaTypeStatusException
     */
    @PostMapping(path = "/unsupported-media-type-status-exception", consumes = MediaType.APPLICATION_XML_VALUE)
    public Mono<Void> unsupportedMediaTypeStatusException() {
        log.info("unsupported media type");
        return Mono.empty();
    }

    /**
     * Handle missing request value exception.
     *
     * @see MissingRequestValueException
     */
    @GetMapping("/missing-request-value-exception")
    public Mono<Void> missingRequestValueException(@RequestParam String id) {
        log.info("missing-request-value id: {}", id);
        return Mono.empty();
    }

    /**
     * Handle unsatisfied request parameter exception.
     *
     * @see UnsatisfiedRequestParameterException
     */
    @GetMapping(path = "/unsatisfied-request-parameter-exception", params = {"type=1", "exist", "!debug"})
    public Mono<Void> unsatisfiedRequestParameterException() {
        log.info("unsatisfied request param");
        return Mono.empty();
    }

    /**
     * Handle web exchange bind exception.
     *
     * @see WebExchangeBindException
     */
    @PostMapping("/web-exchange-bind-exception")
    public Mono<Void> webExchangeBindException(@RequestBody @Validated ProblemDetailRequest problemDetailRequest) {
        log.info("web exchange bind: {}", problemDetailRequest);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for cookie value.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#cookieValue(CookieValue, ParameterValidationResult)
     */
    @GetMapping("/handler-method-validation-exception-cookie-value")
    public Mono<Void> handlerMethodValidationExceptionCookieValue(@CookieValue @NotBlank(message = "cookie 不能为空") String cookieValue) {
        log.info("cookieValue: {}", cookieValue);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for matrix variable.
     *
     * @see HandlerMethodValidationException
     */
    @GetMapping("/handler-method-validation-exception-matrix/{id}")
    public Mono<Void> handlerMethodValidationExceptionMatrix(@PathVariable String id,
                                                             @MatrixVariable @Size(max = 2, message = "list 最大长度是 2") List<String> list) {
        log.info("id: {}, list: {}", id, list);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for model attribute.
     *
     * @see HandlerMethodValidationException
     */
    @GetMapping("/handler-method-validation-exception-model-attribute")
    public Mono<Void> handlerMethodValidationExceptionModelAttribute(@CheckPassword(message = "密码不能是空") ProblemDetailRequest problemDetailRequest) {
        log.info("problemDetailRequest: {}", problemDetailRequest);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for path variable.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#pathVariable(PathVariable, ParameterValidationResult)
     */
    @GetMapping("/handler-method-validation-exception-path-variable/{id}")
    public Mono<Void> handlerMethodValidationExceptionPathVariable(@PathVariable @Size(min = 5, message = "id 长度至少 5") String id) {
        log.info("id: {}", id);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for request body.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#requestBody(RequestBody, ParameterErrors)
     */
    @PostMapping("/handler-method-validation-exception-request-body")
    public Mono<Void> handlerMethodValidationExceptionRequestBody(@RequestBody @CheckPassword(message = "密码不能是空") ProblemDetailRequest problemDetailRequest) {
        log.info("problemDetailRequest: {}", problemDetailRequest);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for request body validation result.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#requestBodyValidationResult(RequestBody, ParameterValidationResult)
     */
    @PostMapping("/handler-method-validation-exception-request-body-validation-result")
    public Mono<Void> handlerMethodValidationExceptionRequestBodyValidationResult(@RequestBody List<@NotBlank(message = "元素不能包含空") String> list) {
        log.info("list: {}", list);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for request header.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#requestHeader(RequestHeader, ParameterValidationResult)
     */
    @GetMapping(path = "/handler-method-validation-exception-request-header")
    public Mono<Void> handlerMethodValidationExceptionRequestHeader(@RequestHeader @NotBlank(message = "header 不能为空") String headerValue) {
        log.info("headerValue: {}", headerValue);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for request param.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#requestParam(RequestParam, ParameterValidationResult)
     */
    @GetMapping("/handler-method-validation-exception-request-param")
    public Mono<Void> handlerMethodValidationExceptionRequestParam(@RequestParam @NotBlank(message = "参数不能为空") String param,
                                                                   @RequestParam @Size(min = 5, message = "长度至少 5") String value) {
        log.info("param: {}, value: {}", param, value);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for request part.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#requestPart(RequestPart, ParameterErrors)
     */
    @PostMapping("/handler-method-validation-exception-request-part")
    public Mono<Void> handlerMethodValidationExceptionRequestPart(@RequestPart(required = false)
                                                                  @CheckFilePart(requiredMessage = "文件不能为空") FilePart filePart) {
        log.info("part: {}", filePart);
        return Mono.empty();
    }

    /**
     * Handle handler method validation exception for other.
     *
     * @see HandlerMethodValidationException
     * @see HandlerMethodValidationException.Visitor#other(ParameterValidationResult)
     */
    @GetMapping("/handler-method-validation-exception-other")
    public Mono<Void> handlerMethodValidationExceptionOther(
            @SessionAttribute(required = false) @NotBlank(message = "sessionAttribute 不能为空") String sessionAttribute,
            @RequestAttribute(required = false) @NotBlank(message = "requestAttribute 不能为空") String requestAttribute,
            @Value("") @NotBlank(message = "value 不能为空") String value) {
        log.info("sessionAttribute: {}, requestAttribute: {}, value: {}", sessionAttribute, requestAttribute, value);
        return Mono.empty();
    }

    /**
     * Handle server web input exception.
     *
     * @see ServerWebInputException
     */
    @GetMapping("/server-web-input-exception")
    public Mono<Void> serverWebInputException() {
        log.info("server web input");
        throw new ServerWebInputException("server web input error");
    }

    /**
     * Handle server error exception.
     *
     * @see ServerErrorException
     */
    @GetMapping("/server-error-exception")
    public Mono<Void> serverErrorException() {
        log.info("server error");
        throw new ServerErrorException("server error", new RuntimeException());
    }

    /**
     * Handle response status exception.
     *
     * @see ResponseStatusException
     */
    @GetMapping("/response-status-exception")
    public Mono<Void> responseStatusException() {
        log.info("response status exception");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exception");
    }

    /**
     * Handle content too large exception.
     *
     * @see ContentTooLargeException
     */
    @PostMapping("/content-too-large-exception")
    public Mono<Void> contentTooLargeException(@RequestBody byte[] body) {
        log.info("body.length: {}", body.length);
        return Mono.empty();
    }

    /**
     * Handle invalid API version exception.
     *
     * @see InvalidApiVersionException
     */
    @GetMapping("/invalid-api-version-exception")
    public Mono<Void> invalidApiVersionException() {
        log.info("not acceptable api version");
        return Mono.empty();
    }

    /**
     * Handle missing API version exception.
     *
     * @see MissingApiVersionException
     */
    @GetMapping("/missing-api-version-exception")
    public Mono<Void> missingApiVersionException() {
        log.info("response status exception missing api version");
        return Mono.empty();
    }

    /**
     * Handle payload too large exception.
     *
     * @see PayloadTooLargeException
     */
    @PostMapping("/payload-too-large-exception")
    public Mono<Void> payloadTooLargeException(@RequestBody byte[] body) {
        log.info("body.length: {}", body.length);
        throw new PayloadTooLargeException(new RuntimeException("payload too large"));
    }

    /**
     * Handle error response exception.
     *
     * @see ErrorResponseException
     */
    @GetMapping("/error-response-exception")
    public Mono<Void> errorResponseException() {
        log.info("error response");
        ExtendedProblemDetail extendedProblemDetail = new ExtendedProblemDetail();
        extendedProblemDetail.setDetail("错误详情");
        extendedProblemDetail.setTitle("错误标题");
        extendedProblemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        extendedProblemDetail.setErrors(Lists.newArrayList(new Error("错误信息 1"), new Error("错误信息 2")));
        throw new ErrorResponseException(HttpStatus.BAD_REQUEST, extendedProblemDetail, new RuntimeException("business exception"));
    }

    /**
     * Handle extended error response exception.
     *
     * @see ExtendedErrorResponseException
     */
    @GetMapping("/extended-error-response-exception")
    public Mono<Void> extendedErrorResponseException() {
        log.info("business");
        ExtendedProblemDetail extendedProblemDetail = new ExtendedProblemDetail();
        extendedProblemDetail.setTitle("支付失败标题");
        extendedProblemDetail.setDetail("支付失败详情");
        extendedProblemDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        extendedProblemDetail.setErrors(Lists.newArrayList(new Error("余额不足"), new Error("支付频繁")));
        throw new ExtendedErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, extendedProblemDetail);
    }

    /**
     * Handle method validation exception.
     *
     * @see MethodValidationException
     */
    @GetMapping("/method-validation-exception")
    public Mono<Void> methodValidationException() {
        log.info("method validation");
        String result = problemDetailService.createProblemDetail("");
        log.info("result: {}", result);
        return Mono.empty();
    }
}
