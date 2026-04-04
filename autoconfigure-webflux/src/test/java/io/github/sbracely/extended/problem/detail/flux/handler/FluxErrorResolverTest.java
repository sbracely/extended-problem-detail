package io.github.sbracely.extended.problem.detail.flux.handler;

import io.github.sbracely.extended.problem.detail.core.logging.ExtendedProblemDetailLog;
import io.github.sbracely.extended.problem.detail.core.response.Error;
import io.github.sbracely.extended.problem.detail.flux.error.resolver.FluxErrorResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FluxErrorResolver} class.
 */
@ExtendWith(MockitoExtension.class)
class FluxErrorResolverTest {

    @Mock
    private ExtendedProblemDetailLog extendedProblemDetailLog;

    private FluxErrorResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new FluxErrorResolver(extendedProblemDetailLog);
    }

    @Test
    void shouldResolveWebExchangeBindException() {
        // Create a binding result with errors
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
                new Object(), "target");
        bindingResult.addError(new ObjectError("object", "Object error message"));
        bindingResult.addError(new FieldError("object", "fieldName", "Field error message"));

        WebExchangeBindException exception = mock(WebExchangeBindException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        List<Error> errors = resolver.resolveWebExchangeBindException(exception);

        assertThat(errors).hasSize(2);
        assertThat(errors.get(0).type()).isEqualTo(Error.Type.PARAMETER);
        assertThat(errors.get(0).target()).isNull();
        assertThat(errors.get(0).message()).isEqualTo("Object error message");
        assertThat(errors.get(1).type()).isEqualTo(Error.Type.PARAMETER);
        assertThat(errors.get(1).target()).isEqualTo("fieldName");
        assertThat(errors.get(1).message()).isEqualTo("Field error message");
    }

    @Test
    void shouldResolveWebExchangeBindExceptionWithFieldErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
                new TestRequest(), "request");
        bindingResult.addError(new FieldError("request", "email", "must be a valid email"));
        bindingResult.addError(new FieldError("request", "password", "size must be between 8 and 20"));

        WebExchangeBindException exception = mock(WebExchangeBindException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        List<Error> errors = resolver.resolveWebExchangeBindException(exception);

        assertThat(errors).hasSize(2);
        assertThat(errors.get(0).target()).isEqualTo("email");
        assertThat(errors.get(0).message()).isEqualTo("must be a valid email");
        assertThat(errors.get(1).target()).isEqualTo("password");
        assertThat(errors.get(1).message()).isEqualTo("size must be between 8 and 20");
    }

    @SuppressWarnings("unused")
    static class TestRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
