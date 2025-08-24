package org.leisureup.global.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.time.*;

public class FlexibleLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonToken token = parser.currentToken();

        if (token == JsonToken.VALUE_NULL) {
            return null;
        }

        if (token == JsonToken.VALUE_STRING) {
            String text = parser.getText();
            if (text == null || text.isBlank()) {
                return null;
            }
            return LocalTime.parse(text);
        }

        if (token == JsonToken.START_OBJECT) {
            JsonNode node = parser.getCodec().readTree(parser);

            int hour = node.hasNonNull("hour") ? node.get("hour").asInt() : 0;
            int minute = node.hasNonNull("minute") ? node.get("minute").asInt() : 0;
            int second = node.hasNonNull("second") ? node.get("second").asInt() : 0;
            int nano = node.hasNonNull("nano") ? node.get("nano").asInt() : 0;

            return LocalTime.of(hour, minute, second, nano);
        }

        return (LocalTime) context.handleUnexpectedToken(LocalTime.class, parser);
    }
}


