package org.leisureup.info.recommend.dto.response;

public record CategoryRecommendation(
        Long categoryId,
        String name,
        String thumbnailUrl
) {

}
