package com.travix.medusa.busyflights.domain.serialize;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ISOInstantDeSerializer extends StdDeserializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public ISOInstantDeSerializer(){
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser jp, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {


       return LocalDateTime.ofInstant(Instant.parse(jp.getText()), ZoneOffset.UTC);
    }
}
