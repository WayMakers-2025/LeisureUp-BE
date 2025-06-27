package org.leisureup.info.recommend.internal.dto.response;

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
