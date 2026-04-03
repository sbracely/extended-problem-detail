package com.github.sbracely.extended.problem.detail.test.flux.request.valid.validator;

import com.github.sbracely.extended.problem.detail.test.flux.request.ProblemDetailRequest;
import com.github.sbracely.extended.problem.detail.test.flux.request.valid.annocation.CheckPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, ProblemDetailRequest> {

    @Override
    public boolean isValid(ProblemDetailRequest problemDetailRequest, ConstraintValidatorContext context) {
        boolean valid = Optional.ofNullable(problemDetailRequest)
                .map(ProblemDetailRequest::getPassword)
                .map(password -> !password.isBlank())
                .orElse(false);
        if (valid) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("password")
                .addConstraintViolation();
        return false;
    }
}
