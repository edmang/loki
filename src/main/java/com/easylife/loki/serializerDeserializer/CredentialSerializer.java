package com.easylife.loki.serializerDeserializer;

import com.easylife.loki.model.Credential;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class CredentialSerializer extends JsonSerializer<Credential> {

    @Override
    public void serialize(Credential credential, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("login", credential.getLogin());
        jsonGenerator.writeStringField("password", credential.getPassword());
        jsonGenerator.writeStringField("description", credential.getDescription());
        jsonGenerator.writeEndObject();
    }
}
