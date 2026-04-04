package io.github.sbracely.extended.problem.detail.flux.handler;

import io.github.sbracely.extended.problem.detail.core.logging.ExtendedProblemDetailLog;
import io.github.sbracely.extended.problem.detail.core.response.Error;
import io.github.sbracely.extended.problem.detail.core.response.ExtendedProblemDetail;
import io.github.sbracely.extended.problem.detail.flux.error.resolver.FluxErrorResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * WebFlux Extended Problem Detail Exception Handler.
 * <p>
 * This exception handler extends Spring WebFlux's {@link ResponseEntityExceptionHandler} to provide
 * enhanced error handling for reactive web applications. It intercepts exceptions
 * and converts them into extended problem detail responses with field-level error information.
 * </p>
 * <p>
 * This handler processes the following types of exceptions:
 * </p>
 * <ul>
 *     <li>{@link WebExchangeBindException} - Data binding exceptions</li>
 *     <li>{@link HandlerMethodValidationException} - Method parameter validation failures using Visitor pattern</li>
 * </ul>
 * <p>
 * To customize error resolving for specific parameter types, extend this class and override
 * the corresponding method in {@link FluxErrorResolver}, or provide a custom
 * {@link FluxErrorResolver} implementation.
 * </p>
 *
 * @see ResponseEntityExceptionHandler
 * @see FluxErrorResolver
 * @since 1.0.0
 */
@RestControllerAdvice
public class FluxExtendedProblemDetailExceptionHandler extends ResponseEntityExceptionHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    protected final FluxErrorResolver errorResolver;

    protected final ExtendedProblemDetailLog extendedProblemDetailLog;

    /**
     * Constructs a new handler with the specified dependencies.
     *
     * @param errorResolver            the FluxErrorResolver instance
     * @param extendedProblemDetailLog the ExtendedProblemDetailLog instance
     */
    public FluxExtendedProblemDetailExceptionHandler(FluxErrorResolver errorResolver,
                                                     ExtendedProblemDetailLog extendedProblemDetailLog) {
        this.errorResolver = errorResolver;
        this.extendedProblemDetailLog = extendedProblemDetailLog;
    }

    /**
     * Handles web exchange bind exceptions.
     * <p>
     * Converts errors from BindingResult into a list of Error objects
     * and wraps them in an ExtendedProblemDetail response.
     * </p>
     *
     * @param ex       the WebExchangeBindException that was thrown
     * @param headers  the HTTP headers to be used in the response
     * @param status   the HTTP status code
     * @param exchange the current server web exchange
     * @return Mono containing ResponseEntity with the ExtendedProblemDetail with errors
     */
    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        List<Error> errors = errorResolver.resolveWebExchangeBindException(ex);
        ExtendedProblemDetail extendedProblemDetail = ExtendedProblemDetail.from(ex.getBody(), errors);
        return handleExceptionInternal(ex, extendedProblemDetail, headers, status, exchange);
    }

    /**
     * Handles handler method validation exceptions using Visitor pattern.
     * <p>
     * This method processes validation results for various parameter annotations by visiting
     * each type of validation result and converting them into Error objects.
     * </p>
     *
     * @param ex       the HandlerMethodValidationException that was thrown
     * @param headers  the HTTP headers to be used in the response
     * @param status   the HTTP status code
     * @param exchange the current server web exchange
     * @return Mono containing ResponseEntity with the ExtendedProblemDetail with errors
     */
    @Override
    protected Mono<ResponseEntity<Object>> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        List<Error> errorList = errorResolver.resolveHandlerMethodValidationException(ex);
        ExtendedProblemDetail extendedProblemDetail = ExtendedProblemDetail.from(ex.getBody(), errorList);
        return handleExceptionInternal(ex, extendedProblemDetail, headers, status, exchange);
    }

    /**
     * Handles method validation exceptions.
     * <p>
     * Converts method-level errors into a list of Error objects,
     * logs the failure, and wraps them in an ExtendedProblemDetail response.
     * </p>
     *
     * @param ex       the MethodValidationException that was thrown
     * @param status   the HTTP status code
     * @param exchange the current server web exchange
     * @return Mono containing ResponseEntity with the ExtendedProblemDetail with errors
     */
    @Override
    protected Mono<ResponseEntity<Object>> handleMethodValidationException(MethodValidationException ex, HttpStatus status, ServerWebExchange exchange) {
        List<Error> errors = errorResolver.resolveMethodValidationException(ex);
        String method = ex.getMethod().getName();
        extendedProblemDetailLog.log(logger, ex, "handleMethodValidationException method = {}, errors = {}", method, errors);
        ProblemDetail body = createProblemDetail(ex, status, "Validation failed", null, null, exchange);
        ExtendedProblemDetail extendedProblemDetail = ExtendedProblemDetail.from(body, errors);
        return handleExceptionInternal(ex, extendedProblemDetail, null, status, exchange);
    }
}
