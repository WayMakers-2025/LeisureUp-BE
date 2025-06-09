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
}
