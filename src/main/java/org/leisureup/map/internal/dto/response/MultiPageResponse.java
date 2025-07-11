package org.leisureup.map.internal.dto.response;

import java.util.*;

public record MultiPageResponse<T>(
        int pageNo, int pageSize,
        int numOfPageResponse,
        int numOfGivenElements,
        Map<String, PageResponse<T>> elements
) {

    public static <T> MultiPageResponse<T> of(
            int pageNo, int pageSize, Map<String, PageResponse<T>> elements
    ) {

        elements = new HashMap<>(
                elements == null ? Collections.emptyMap() : elements
        );

        int numOfPageResponse = elements.size();
        int numOfGivenElements = getNumOfInnerElements(elements);

        return new MultiPageResponse<>(
                pageNo, pageSize, numOfPageResponse, numOfGivenElements,
                elements
        );
    }

    private static <T> int getNumOfInnerElements(
            Map<?, PageResponse<T>> map
    ) {
        int sum = 0;

        if (map != null) {
            for (var pageResponse : map.values()) {
                sum += pageResponse.numOfTotalElements();
            }
        }

        return sum;
    }
}
