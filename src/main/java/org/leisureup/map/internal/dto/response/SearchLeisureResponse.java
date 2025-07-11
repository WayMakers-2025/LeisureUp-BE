package org.leisureup.map.internal.dto.response;

import io.swagger.v3.oas.annotations.media.*;
import java.time.*;
import org.leisureup.map.internal.dto.api.*;

public record SearchLeisureResponse(
        Long contentId,
        String title,
        LocationInfo locationInfo,
        MinorInfo minorInfo
) {

    public static SearchLeisureResponse of(LocationBaseLeisureSearch apiResp) {
        Long contentId = apiResp.contentId();
        String title = apiResp.title();

        var locationInfo = LocationInfo.of(
                apiResp.mapX(), apiResp.mapY(), apiResp.dist(),
                apiResp.address(), apiResp.detailedAddress(),
                apiResp.zipcode()
        );

        var minor = MinorInfo.of(
                apiResp.address(), apiResp.detailedAddress(),
                apiResp.zipcode(),
                apiResp.createdTime(), apiResp.modifiedTime()
        );

        return new SearchLeisureResponse(contentId, title, locationInfo, minor);
    }

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    @Schema(name = "LocationInfoOnSearchLeisure")
    public record LocationInfo(
            double gpsX, double gpsY, double dist,
            String briefAddress, String detailedAddress,
            String zipcode
    ) {

        public static LocationInfo of(
                double mapX, double mapY, double dist,
                String briefAddress, String detailedAddress, String zipcode
        ) {
            return new LocationInfo(
                    mapX, mapY, dist,
                    emptyIfNull(briefAddress), emptyIfNull(detailedAddress),
                    emptyIfNull(zipcode)
            );
        }
    }

    public record MinorInfo(
            String telephone,
            String largeThumbnail,
            String smallThumbnail,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {

        public static MinorInfo of(
                String telephone, String largeTh, String smallTh,
                LocalDateTime createdAt, LocalDateTime modifiedAt
        ) {
            return new MinorInfo(
                    emptyIfNull(telephone), emptyIfNull(largeTh), emptyIfNull(smallTh),
                    createdAt, modifiedAt
            );
        }
    }
}
