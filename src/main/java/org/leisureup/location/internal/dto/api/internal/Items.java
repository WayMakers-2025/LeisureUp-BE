package org.leisureup.location.internal.dto.api.internal;

import com.fasterxml.jackson.databind.annotation.*;
import java.util.*;

// Generic 자체는 문제 없는데, 요청 관련 정보 없으면 API 응답이 {"items" : ""} 처럼 제공되서
// custom deserializer 가 필요함.
@JsonDeserialize(using = ItemsDeserializer.class)
public record Items<I>(
        List<I> item
) {

}
