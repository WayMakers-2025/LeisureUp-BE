package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalCategoryInfo {

    @Column(length = 1000)
    private String thumbnailUrl;

    @Column(length = 1000)
    private String notification;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private SuitableSeason season = SuitableSeason.ANY;

    public static AdditionalCategoryInfo of(
            String thumbnailUrl, String notification,
            String description, SuitableSeason season
    ) {
        return new AdditionalCategoryInfo(
                thumbnailUrl, notification, description, season
        );
    }

    public static AdditionalCategoryInfo of(AdditionalCategoryInfo given) {
        return new AdditionalCategoryInfo(
                given.getThumbnailUrl(), given.getNotification(),
                given.getDescription(), given.getSeason()
        );
    }

    public enum SuitableSeason {
        SPRING, SUMMER, AUTUMN, WINTER, ANY
    }
}
