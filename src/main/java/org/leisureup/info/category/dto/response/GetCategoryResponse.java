package org.leisureup.info.category.dto.response;

public record GetCategoryResponse(
        Long id,
        String name,
        String categoryCode,
        Kind kind,
        String description,
        String target,
        String requiredGear,
        String warning,
        String thumbnailUrl
) {

    public enum Kind {
        EARTH, WATER, SKY, OTHER
    }
}
