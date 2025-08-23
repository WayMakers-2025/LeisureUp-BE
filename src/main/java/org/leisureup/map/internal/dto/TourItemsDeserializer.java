package org.leisureup.map.internal.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Collections;

/**
 * TourAPI 응답에서 body.items 가 ""(빈 문자열)로 오는 경우를 빈 리스트로 처리한다.
 */
public class TourItemsDeserializer extends JsonDeserializer<TourApiResponse.Items> {

    @Override
    public TourApiResponse.Items deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();

        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (text == null || text.isBlank()) {
                return new TourApiResponse.Items(Collections.emptyList());
            }
        }

        if (token == JsonToken.VALUE_NULL) {
            return new TourApiResponse.Items(Collections.emptyList());
        }

        JsonNode node = p.getCodec().readTree(p);
        if (node.isTextual() && node.asText().isBlank()) {
            return new TourApiResponse.Items(Collections.emptyList());
        }

        // 정상 케이스는 Jackson 기본 매핑에 위임하기 위해 다시 파싱
        // node 안의 item 배열을 그대로 사용
        JsonNode itemNode = node.get("item");
        if (itemNode == null || itemNode.isNull()) {
            return new TourApiResponse.Items(Collections.emptyList());
        }

        // 기본 매퍼를 재사용할 수 없으므로, 동일 파서로는 어려워 간단히 유형 오류 없이 빈 목록만 보장
        // 실제 요소 역직렬화는 상위 ObjectMapper가 처리하므로 여기서는 빈 처리만 특수화
        return (TourApiResponse.Items) ctxt.readTreeAsValue(node, TourApiResponse.Items.class);
    }
}


