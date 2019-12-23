package com.travix.medusa.busyflights.domain.serialize;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ISOLocalDateTimeDeSerializer extends StdDeserializer<LocalDateTime> {

    private static final long serialVersionUID = 1L;

    public ISOLocalDateTimeDeSerializer(){
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser jp, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

       return LocalDateTime.parse(jp.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
