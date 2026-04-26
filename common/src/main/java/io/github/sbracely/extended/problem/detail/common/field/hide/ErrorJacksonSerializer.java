package io.github.sbracely.extended.problem.detail.common.field.hide;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.sbracely.extended.problem.detail.common.response.Error;

/**
 * Jackson serializer for Error entries that applies field visibility rules.
 *
 * @since 3.0.0
 */
public class ErrorJacksonSerializer extends StdSerializer<Error> {

    /**
     * Effective visibility rules for fields in {@code errors[]} entries.
     */
    private final ProblemDetailFieldVisibility fieldVisibility;

    /**
     * Creates a serializer that applies the given field visibility rules.
     *
     * @param fieldVisibility the effective field visibility rules
     */
    public ErrorJacksonSerializer(ProblemDetailFieldVisibility fieldVisibility) {
        super(Error.class);
        this.fieldVisibility = fieldVisibility;
    }

    @Override
    public void serialize(Error value, JsonGenerator gen, SerializerProvider provider) throws java.io.IOException {
        gen.writeStartObject();
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("type"), "type", value.type());
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("target"), "target", value.target());
        writeField(gen, provider, fieldVisibility.isErrorFieldVisible("message"), "message", value.message());
        gen.writeEndObject();
    }

    private static void writeField(
            JsonGenerator gen,
            SerializerProvider provider,
            boolean visible,
            String fieldName,
            Object value) throws java.io.IOException {
        if (!visible || value == null) {
            return;
        }
        if (value instanceof CharSequence charSequence && charSequence.isEmpty()) {
            return;
        }
        gen.writeFieldName(fieldName);
        provider.defaultSerializeValue(value, gen);
    }
}
