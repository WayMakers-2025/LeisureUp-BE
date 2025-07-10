package org.leisureup.map.internal.dto.response;

import java.time.*;
import org.leisureup.map.internal.dto.api.*;

public record SearchLeisureResponse(
        Long contentId,
        String title,
        CoordinateInfo coordinateInfo,
        MajorInfo majorInfo,
        MinorInfo minorInfo
) {

    public static SearchLeisureResponse of(LocationBaseLeisureSearch apiResp) {
        Long contentId = apiResp.contentId();
        String title = apiResp.title();

        var cord = CoordinateInfo.of(
                apiResp.mapX(),
                apiResp.mapY(),
                apiResp.dist()
        );

        var major = MajorInfo.of(
                apiResp.tel(),
                apiResp.firstImage(),
                apiResp.firstImage2()
        );

        var minor = MinorInfo.of(
                apiResp.address(), apiResp.detailedAddress(),
                apiResp.zipcode(),
                apiResp.createdTime(), apiResp.modifiedTime()
        );

        return new SearchLeisureResponse(contentId, title, cord, major, minor);
    }

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    public record CoordinateInfo(
            double x, double y, double dist
    ) {

        public static CoordinateInfo of(double x, double y, double dist) {
            return new CoordinateInfo(x, y, dist);
        }
    }

    public record MajorInfo(
            String telephone,
            String largeThumbnail,
            String smallThumbnail
    ) {

        public static MajorInfo of(String tel, String th1, String th2) {
            return new MajorInfo(
                    emptyIfNull(tel), emptyIfNull(th1), emptyIfNull(th2)
            );
        }
    }

    public record MinorInfo(
            String briefAddress,
            String detailedAddress,
            String zipcode,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {

        public static MinorInfo of(
                String add1, String add2, String zip,
                LocalDateTime createdAt, LocalDateTime modifiedAt
        ) {
            return new MinorInfo(
                    emptyIfNull(add1), emptyIfNull(add2), emptyIfNull(zip),
                    createdAt, modifiedAt
            );
        }
    }
}
