package org.leisureup.map.internal.dto.request;

import org.leisureup.map.internal.dto.request.SearchLeisureRequest.*;

public record PagingInfo(
        int pageNo, int pageSize, Sort sorting
) {

}
