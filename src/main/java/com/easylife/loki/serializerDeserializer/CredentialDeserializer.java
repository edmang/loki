package com.easylife.loki.serializerDeserializer;

import com.easylife.loki.model.Credential;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CredentialDeserializer extends JsonDeserializer<Credential> {

    @Override
    public Credential deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String login = node.get("login").asText();
        String password = node.get("password").asText();
        String description = node.get("description").asText();
        Credential credential = new Credential();
        credential.setLogin(login);
        credential.setPassword(password);
        credential.setDescription(description);
        return credential;
    }
}
