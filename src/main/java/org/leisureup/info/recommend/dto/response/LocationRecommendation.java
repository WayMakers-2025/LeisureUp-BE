package org.leisureup.info.recommend.dto.response;

public record LocationRecommendation(
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
