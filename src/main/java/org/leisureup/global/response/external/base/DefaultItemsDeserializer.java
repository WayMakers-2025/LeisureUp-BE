package org.leisureup.global.response.external.base;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import java.io.*;
import java.util.*;

public class DefaultItemsDeserializer<I>
        extends JsonDeserializer<Items<I>> implements ContextualDeserializer {

    private JavaType genericItemType;

    @Override
    public Items<I> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode itemsNode = p.getCodec().readTree(p);

        // 만약 {"items" : ""} 가 들어오거나 존재하지 않으면 빈 items 를 반환한다.
        if (
                itemsNode == null ||
                (itemsNode.isTextual() && itemsNode.asText().isBlank())
        ) {
            return new Items<>(Collections.emptyList());
        }

        // 만약 {"items" : {"item" : []}} 가 들어오거나 존재하지 않으면 빈 items 를 반환한다.
        JsonNode itemNode = itemsNode.get("item");
        if (
                itemNode == null || itemNode.isNull() ||
                itemNode.isEmpty()
        ) {
            return new Items<>(Collections.emptyList());
        }

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JavaType listType = mapper.getTypeFactory()
                .constructCollectionType(List.class, genericItemType);
        List<I> items;

        if (itemNode.isArray()) {
            items = mapper.readerFor(listType).readValue(itemNode);
        } else {
            // Single object case
            I singleItem = mapper.readerFor(genericItemType).readValue(itemNode);
            items = List.of(singleItem);
        }

        return new Items<>(items);
    }

    @Override
    public JsonDeserializer<?> createContextual(
            DeserializationContext ctxt, BeanProperty property
    ) {
        JavaType wrapperType = property.getType(); // Items<I>
        JavaType genericItemType = wrapperType.containedType(0); // I
        DefaultItemsDeserializer<?> deserializer = new DefaultItemsDeserializer<>();
        deserializer.genericItemType = genericItemType;
        return deserializer;
    }
}
