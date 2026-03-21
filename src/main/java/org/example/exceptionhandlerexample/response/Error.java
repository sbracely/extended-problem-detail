package org.example.exceptionhandlerexample.response;

import lombok.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Error {
    private Type type;
    private String field;
    private String message;

    public Error(ObjectError objectError) {
        if (objectError instanceof FieldError fieldError) {
            this.field = fieldError.getField();
        }
        this.message = objectError.getDefaultMessage();
        this.type = Type.PARAMETER;
    }

    public Error(String field, String message, Type type) {
        this.field = field;
        this.message = message;
        this.type = type;
    }

    public enum Type {
        PARAMETER,
        COOKIE,
        HEADER,
    }
}