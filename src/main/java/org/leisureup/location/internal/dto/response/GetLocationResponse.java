package org.leisureup.location.internal.dto.response;

public record GetLocationResponse(
        Long id, String name,
        double gpsX, double gpsY,
        String briefAddress, String detailedAddress, String zipcode,

        String introduction,
        String homepageUrl, String telephone,
        String largeThumbnail, String smallThumbnail,

        Category category
) {

    public enum Category {
        EARTH, WATER, SKY, OTHER
    }

    public static GetLocationResponse of(
            Long id, String name, double gpsX, double gpsY,
            String add1, String add2, String zipcode,
            String intro, String homepage, String tel,
            String thumb1, String thumb2,
            String categoryType
    ) {

        Category cat = switch (categoryType) {
            case "EARTH" -> Category.EARTH;
            case "WATER" -> Category.WATER;
            case "SKY" -> Category.SKY;
            default -> Category.OTHER;
        };

        return new GetLocationResponse(
                id, name, gpsX, gpsY, add1, add2, zipcode,
                intro, homepage, tel, thumb1, thumb2, cat
        );
    }
}
