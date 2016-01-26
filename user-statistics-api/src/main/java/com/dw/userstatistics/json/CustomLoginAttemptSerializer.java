package com.dw.userstatistics.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by Alberto on 2015-09-27.
 */
public class CustomLoginAttemptSerializer extends StdScalarSerializer<DateTime> {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss.SSS");


    public CustomLoginAttemptSerializer(){
        super(DateTime.class);
    }

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonGenerationException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("date", DATE_FORMAT.print(dateTime));
        jsonGenerator.writeStringField("time", TIME_FORMAT.print(dateTime));
        jsonGenerator.writeEndObject();

    }
}
