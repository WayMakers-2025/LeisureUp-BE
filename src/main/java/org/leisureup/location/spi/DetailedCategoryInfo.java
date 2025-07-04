package org.leisureup.location.spi;

public record DetailedCategoryInfo(
        Long id,
        String categoryCode,
        String name,
        Cat category,
        String thumbnailUrl,
        String notification,
        String description,
        String recommendingCode
) {

}
