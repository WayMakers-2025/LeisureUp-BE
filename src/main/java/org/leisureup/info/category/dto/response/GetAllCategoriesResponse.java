package org.leisureup.info.category.dto.response;

import java.util.*;

public record GetAllCategoriesResponse(
        List<CategoryInfoResponse> earth,
        List<CategoryInfoResponse> water,
        List<CategoryInfoResponse> sky
) {

    public static GetAllCategoriesResponse of(
            List<CategoryInfoResponse> earths,
            List<CategoryInfoResponse> waters,
            List<CategoryInfoResponse> skies
    ) {
        return new GetAllCategoriesResponse(earths, waters, skies);
    }
}

