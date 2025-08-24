package org.leisureup.member.internal.dto.response;

import java.util.*;
import org.springframework.data.domain.*;

public record PageResponse<T>(
        long totalPages,
        int pageNo,
        int pageSize,
        boolean lastPage,
        List<T> elements
) {

    public static <T> PageResponse<T> of(Page<?> page, List<T> elements) {
        return new PageResponse<>(
                page.getTotalPages(), page.getNumber(),
                page.getSize(), page.isLast(),
                elements
        );
    }
}
