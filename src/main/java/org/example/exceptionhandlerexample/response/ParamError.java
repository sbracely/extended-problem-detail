package org.example.exceptionhandlerexample.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ParamError {
    private String field;
    private String message;

    public ParamError(ObjectError objectError) {
        if (objectError instanceof FieldError fieldError) {
            this.field = fieldError.getField();
        }
        this.message = objectError.getDefaultMessage();
    }

    public ParamError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static List<ParamError> processParameterValidationResult(ParameterValidationResult parameterValidationResult) {
        String parameterName = parameterValidationResult.getMethodParameter().getParameterName();
        return parameterValidationResult.getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .map(message -> new ParamError(parameterName, message))
                .collect(Collectors.toList());
    }
}