package io.github.sbracely.extended.problem.detail.common.field.hide;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.sbracely.extended.problem.detail.common.response.Error;
import io.github.sbracely.extended.problem.detail.common.response.ExtendedProblemDetail;
import org.springframework.http.ProblemDetail;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ProblemDetailJacksonSerializerSupport {

    private static final Set<String> STANDARD_FIELDS = Set.of("type", "title", "status", "detail", "instance");

    private ProblemDetailJacksonSerializerSupport() {
    }

    static void writeProblemDetail(
            ProblemDetail value,
            JsonGenerator gen,
            SerializerProvider provider,
            ProblemDetailFieldVisibility fieldVisibility) throws java.io.IOException {
        gen.writeStartObject();
        writeField(gen, provider, fieldVisibility.isTypeVisible(), "type", value.getType());
        writeField(gen, provider, fieldVisibility.isTitleVisible(), "title", value.getTitle());
        if (fieldVisibility.isStatusVisible()) {
            gen.writeNumberField("status", value.getStatus());
        }
        writeField(gen, provider, fieldVisibility.isDetailVisible(), "detail", value.getDetail());
        writeField(gen, provider, fieldVisibility.isInstanceVisible(), "instance", value.getInstance());
        if (value instanceof ExtendedProblemDetail extendedProblemDetail) {
            writeErrors(gen, provider, fieldVisibility, extendedProblemDetail.getErrors());
        }
        Map<String, Object> properties = value.getProperties();
        if (properties != null) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String fieldName = entry.getKey();
                if (STANDARD_FIELDS.contains(fieldName)) {
                    continue;
                }
                if (value instanceof ExtendedProblemDetail && "errors".equals(fieldName)) {
                    continue;
                }
                writeField(gen, provider, fieldVisibility.isVisible(fieldName), fieldName, entry.getValue());
            }
        }
        gen.writeEndObject();
    }

    private static void writeErrors(
            JsonGenerator gen,
            SerializerProvider provider,
            ProblemDetailFieldVisibility fieldVisibility,
            List<Error> errors) throws java.io.IOException {
        if (!fieldVisibility.isErrorsVisible() || isEmpty(errors)) {
            return;
        }
        gen.writeFieldName("errors");
        gen.writeStartArray();
        for (Error error : errors) {
            if (error == null) {
                gen.writeNull();
                continue;
            }
            writeError(gen, provider, fieldVisibility, error);
        }
        gen.writeEndArray();
    }

    private static void writeError(
            JsonGenerator gen,
            SerializerProvider provider,
            ProblemDetailFieldVisibility fieldVisibility,
            Error error) throws java.io.IOException {
        gen.writeStartObject();
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("type"), "type", error.type());
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("target"), "target", error.target());
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("message"), "message", error.message());
        gen.writeEndObject();
    }

    private static void writeField(
            JsonGenerator gen,
            SerializerProvider provider,
            boolean visible,
            String fieldName,
            Object value) throws java.io.IOException {
        if (!visible || isEmpty(value)) {
            return;
        }
        gen.writeFieldName(fieldName);
        provider.defaultSerializeValue(value, gen);
    }

    private static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof CharSequence charSequence) {
            return charSequence.isEmpty();
        }
        if (value instanceof Collection<?> collection) {
            return collection.isEmpty();
        }
        if (value instanceof Map<?, ?> map) {
            return map.isEmpty();
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        return false;
    }
}
