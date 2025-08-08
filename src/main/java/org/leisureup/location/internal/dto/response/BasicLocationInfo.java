package org.leisureup.location.internal.dto.response;

import org.leisureup.location.internal.dto.*;

public record BasicLocationInfo(
        Long id, String name,
        double gpsX, double gpsY,
        String briefAddress, String detailedAddress, String zipcode,

        String introduction,
        String homepageUrl, String telephone,
        String largeThumbnail, String smallThumbnail,

        Category category,
        LocationType locationType
) {

    public static BasicLocationInfo of(
            Long id, String name, double gpsX, double gpsY,
            String add1, String add2, String zipcode,
            String intro, String homepage, String tel,
            String thumb1, String thumb2,
            String categoryType, LocationType locationType
    ) {

        Category cat = switch (categoryType) {
            case "EARTH" -> Category.EARTH;
            case "WATER" -> Category.WATER;
            case "SKY" -> Category.SKY;
            default -> Category.OTHER;
        };

        return new BasicLocationInfo(
                id, name, gpsX, gpsY, add1, add2, zipcode,
                intro, homepage, tel, thumb1, thumb2, cat,
                locationType
        );
    }

    public enum Category {
        EARTH, WATER, SKY, OTHER
    }
}
