package org.leisureup.member.internal.dto.response;

import org.leisureup.location.spi.*;

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

    public static PickLocation of(LocationResponse lResp) {

        Long id = lResp.locationId();
        String name = lResp.title();

        Desc desc = lResp.description();
        String th1 = desc != null ? desc.largeThumbnail() : "";
        String th2 = desc != null ? desc.smallThumbnail() : "";

        Category category;
        Cat cat = lResp.category();

        if (cat == null) {
            category = Category.OTHER;
        } else {
            category = switch (cat) {
                case EARTH -> Category.EARTH;
                case WATER -> Category.WATER;
                case SKY -> Category.SKY;
                default -> Category.OTHER;
            };
        }

        return new PickLocation(id, name, th1, th2, category);
    }
}
