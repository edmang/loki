package com.easylife.loki.serializerDeserializer;

import com.easylife.loki.model.Credential;
import com.easylife.loki.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String username = node.get("username").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        Credential[] credentials = objectMapper.readValue(jsonParser, Credential[].class);

        User user = new User();
        user.setUsername(username);
        user.setCredentials(Arrays.asList(credentials));
        return user;
    }
}
