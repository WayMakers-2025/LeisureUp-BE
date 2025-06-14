package org.leisureup.location.internal.dto.api.internal;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import java.io.*;
import java.util.*;

// 아니 이게 왜 되는거지..... 진짜 모르겠다.. ㅠㅠㅠㅠㅠㅠ
// 참고 : https://stackoverflow.com/questions/36159677/how-to-create-a-custom-deserializer-in-jackson-for-a-generic-type
public class ItemsDeserializer<I>
        extends JsonDeserializer<Items<I>> implements ContextualDeserializer {

    private JavaType genericItemType;

    @Override
    public Items<I> deserialize(JsonParser p, DeserializationContext context)
            throws IOException, JacksonException {

        JsonNode itemsNode = p.getCodec().readTree(p);

        // 만약 {"items" : ""} 가 들어오거나 존재하지 않으면 빈 items 를 반환한다.
        if (itemsNode == null || (itemsNode.isTextual() && itemsNode.asText().isBlank())) {
            return new Items<>(Collections.emptyList());
        }

        // 만약 {"items" : {"item" : []}} 가 들어오거나 존재하지 않으면 빈 items 를 반환한다.
        JsonNode itemNode = itemsNode.get("item");
        if (itemNode == null || itemNode.isNull() || itemNode.isEmpty()) {
            return new Items<>(Collections.emptyList());
        }

        // 여기 이후로는 도대체 뭘 하는건지 잘 모르겠다... ㅠ
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
            DeserializationContext context, BeanProperty property
    ) throws JsonMappingException {
        JavaType wrapperType = property.getType(); // Items<I>
        JavaType genericItemType = wrapperType.containedType(0); // I
        ItemsDeserializer<?> objectItemsDeserializer = new ItemsDeserializer<>();
        objectItemsDeserializer.genericItemType = genericItemType;
        return objectItemsDeserializer;
    }
}
