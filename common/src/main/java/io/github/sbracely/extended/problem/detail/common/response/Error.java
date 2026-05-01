package io.github.sbracely.extended.problem.detail.common.response;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ProblemDetail;

/**
 * Represents a detailed error information in the extended problem detail response.
 * <p>
 * This record encapsulates individual error details including error type, target,
 * and error message. It is used to provide detailed error information in API responses,
 * supporting both validation errors and business logic errors.
 * </p>
 *
 * @param type    the type of error (for example QUERY_PARAMETER, REQUEST_BODY, COOKIE, HEADER, or BUSINESS)
 * @param target  the target of the error, such as field name, resource name, or business entity
 * @param message the error message describing what went wrong
 * @see ProblemDetail
 * @since 1.0.0
 */
public record Error(@Nullable Type type, @Nullable String target, @Nullable String message) {

    /**
     * Enumeration of error types indicating the source of the error.
     * <ul>
     *     <li>{@code QUERY_PARAMETER} - Error from request query parameter</li>
     *     <li>{@code PATH_VARIABLE} - Error from path variable</li>
     *     <li>{@code REQUEST_BODY} - Error from request body</li>
     *     <li>{@code MODEL_ATTRIBUTE} - Error from model attribute binding</li>
     *     <li>{@code REQUEST_PART} - Error from multipart request part</li>
     *     <li>{@code MATRIX_VARIABLE} - Error from matrix variable</li>
     *     <li>{@code COOKIE} - Error from cookie value</li>
     *     <li>{@code HEADER} - Error from request header</li>
     *     <li>{@code BUSINESS} - Error from business logic</li>
     * </ul>
     */
    public enum Type {
        /**
         * Error from request query parameter.
         */
        QUERY_PARAMETER,
        /**
         * Error from path variable.
         */
        PATH_VARIABLE,
        /**
         * Error from request body.
         */
        REQUEST_BODY,
        /**
         * Error from model attribute binding.
         */
        MODEL_ATTRIBUTE,
        /**
         * Error from multipart request part.
         */
        REQUEST_PART,
        /**
         * Error from matrix variable.
         */
        MATRIX_VARIABLE,
        /**
         * Error from cookie value.
         */
        COOKIE,
        /**
         * Error from request header.
         */
        HEADER,
        /**
         * Error from business logic.
         */
        BUSINESS,
    }
}
