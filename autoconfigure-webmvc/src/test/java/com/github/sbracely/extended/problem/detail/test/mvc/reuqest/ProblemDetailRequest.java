package com.github.sbracely.extended.problem.detail.test.mvc.reuqest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.github.sbracely.extended.problem.detail.test.mvc.reuqest.valid.annocation.ConfirmPassword;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
@ConfirmPassword(message = "Password and confirm password do not match", fields = {"password", "confirmPassword"})
public class ProblemDetailRequest {

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    @Length(min = 6, max = 10, message = "Name length must be between 6-10")
    private String name;

    @NotNull(message = "Age cannot be null")
    @Range(min = 0, max = 150, message = "Age range is 0-150")
    private Integer age;

    private String password;
    private String confirmPassword;
}
