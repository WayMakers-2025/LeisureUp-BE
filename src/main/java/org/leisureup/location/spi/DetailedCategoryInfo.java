package org.leisureup.location.spi;

public record DetailedCategoryInfo(
        Long id,
        String categoryCode,
        String name,
        Cat category,
        String briefInfo,
        String target,
        String requiredGear,
        String warning,
        String thumbnailUrl,
        String recommendingCode
) {

}
