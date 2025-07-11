package org.leisureup.map.internal.dto.response;

import java.util.*;

public record PageResponse<T>(
        int pageNo, int pageSize,
        int numOfElements,
        int numOfTotalElements,
        List<T> elements
) {

    public static <T> PageResponse<T> of(
            int pageNo, int pageSize,
            int numOfTotalElements, List<T> elements
    ) {
        if (elements == null) {
            elements = Collections.emptyList();
        }
        return new PageResponse<>(
                pageNo, pageSize, elements.size(),
                numOfTotalElements, elements
        );
    }
}
