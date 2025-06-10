package org.leisureup.member.internal.dto.response;

import java.util.*;

public record PageResponse<T>(
        long totalPages,
        int pageNo,
        int pageSize,
        boolean lastPage,
        List<T> elements
) {

}
