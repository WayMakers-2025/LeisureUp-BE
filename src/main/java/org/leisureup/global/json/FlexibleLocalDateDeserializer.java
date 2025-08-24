package org.leisureup.global.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDate;

public class FlexibleLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();

        if (token == JsonToken.VALUE_NULL) {
            return null;
        }

        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (text == null || text.isBlank()) {
                return null;
            }
            // ISO-8601 yyyy-MM-dd
            return LocalDate.parse(text);
        }

        if (token == JsonToken.START_OBJECT) {
            JsonNode node = p.getCodec().readTree(p);
            int year = node.hasNonNull("year") ? node.get("year").asInt() : 0;
            int month = node.hasNonNull("month") ? node.get("month").asInt() : 1;
            int day = node.hasNonNull("day") ? node.get("day").asInt() : 1;
            return LocalDate.of(year, month, day);
        }

        return (LocalDate) ctxt.handleUnexpectedToken(LocalDate.class, p);
    }
}


