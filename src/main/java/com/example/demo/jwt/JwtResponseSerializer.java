package com.example.demo.jwt;

import com.example.demo.payload.response.JwtResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class JwtResponseSerializer extends StdSerializer<JwtResponse> {

    public JwtResponseSerializer() {
        this(null);
    }

    public JwtResponseSerializer(Class<JwtResponse> t) {
        super(t);
    }

    @Override
    public void serialize(JwtResponse jwtResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("token",jwtResponse.getType()+" "+jwtResponse.getToken());
        jsonGenerator.writeStringField("username", jwtResponse.getUsername());
        jsonGenerator.writeNumberField("id", jwtResponse.getId());
        jsonGenerator.writeEndObject();
    }
}
