package org.leisureup.info.category.dto.response;

public record GetCategoryResponse(
        Long id,
        String name,
        String categoryCode,
        Kind kind,
        String thumbnailUrl,
        String notification,
        String description
) {

    public enum Kind {
        EARTH, WATER, SKY, OTHER
    }
}
