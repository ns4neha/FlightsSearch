package com.travix.medusa.busyflights.domain.serialize;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ISOInstantSerializer extends StdSerializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public ISOInstantSerializer(){
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider sp) throws IOException, JsonProcessingException {
        Instant instant = value.toInstant(ZoneOffset.UTC);
        gen.writeString(instant.toString());
    }
}
