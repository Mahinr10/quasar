package com.personal.quasar.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializer extends JsonSerializer<Date> {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    static {
        FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(FORMATTER.format(date));
    }
}
