package com.personal.quasar.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateDeserializer extends JsonDeserializer<Date> {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    static {
        FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC")); // Parse as UTC
    }

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateString = parser.getText();
        try {
            return FORMATTER.parse(dateString);
        } catch (Exception e) {
            throw new IOException("Failed to parse date: " + dateString, e);
        }
    }
}
