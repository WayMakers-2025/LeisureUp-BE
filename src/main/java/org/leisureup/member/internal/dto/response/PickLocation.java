package org.leisureup.member.internal.dto.response;

public record PickLocation(
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
