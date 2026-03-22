package org.example.exceptionhandlerexample.response;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Objects;

public class Error {
    private Type type;
    private String field;
    private String message;

    public Error() {
    }

    public Error(String field, String message, Type type) {
        this.field = field;
        this.message = message;
        this.type = type;
    }

    public Error(ObjectError objectError) {
        if (objectError instanceof FieldError fieldError) {
            this.field = fieldError.getField();
        }
        this.message = objectError.getDefaultMessage();
        this.type = Type.PARAMETER;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Error error = (Error) o;
        return type == error.type && Objects.equals(field, error.field) && Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, field, message);
    }

    @Override
    public String toString() {
        return "Error{" +
                "type=" + type +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public enum Type {
        PARAMETER,
        COOKIE,
        HEADER,
    }
}