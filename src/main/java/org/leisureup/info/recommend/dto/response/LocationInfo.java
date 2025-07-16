package org.leisureup.info.recommend.dto.response;

import io.swagger.v3.oas.annotations.media.*;

@Schema(name = "LocationInfoOnRecommend")
public record LocationInfo(
        Long id,
        String name,
        String largeThumbnail,
        String smallThumbnail,
        Category category
) {

    public enum Category {
        EARTH, WATER, SKY, OTHER
    }
}
