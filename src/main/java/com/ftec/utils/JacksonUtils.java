package com.ftec.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class JacksonUtils {

    public static class BigDoubleDeserializer extends JsonSerializer<Double> {
        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            NumberFormat d = new DecimalFormat("#.########################");
            gen.writeNumber(d.format(value));
        }

    }

    public static class SmallDoubleDeserializer extends JsonSerializer<Double> {
        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            NumberFormat d = new DecimalFormat("#.########");
            gen.writeNumber(d.format(value));
        }

    }
}
