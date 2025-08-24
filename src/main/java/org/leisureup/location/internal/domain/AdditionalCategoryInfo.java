package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalCategoryInfo {

    @Column(length = 500)
    private String briefInfo;

    @Column(length = 200)
    private String categoryTarget;

    @Column(length = 200)
    private String requiredGear;

    @Column(length = 1000)
    private String warning;

    @Column(length = 1000)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private List<SuitableSeason> suitableSeasons = List.of(SuitableSeason.ANY);


    protected AdditionalCategoryInfo(
            String briefInfo, String categoryTarget, String requiredGear,
            String warning, String thumbnailUrl, List<SuitableSeason> suitableSeasons
    ) {
        this.briefInfo = briefInfo;
        this.categoryTarget = categoryTarget;
        this.requiredGear = requiredGear;
        this.warning = warning;
        this.thumbnailUrl = thumbnailUrl;
        this.suitableSeasons = suitableSeasons;
    }

    public static AdditionalCategoryInfo of(
            String information, String categoryTarget, String requiredGear,
            String notification, String thumbnailUrl, List<SuitableSeason> suitableSeasons
    ) {
        return new AdditionalCategoryInfo(
                information, categoryTarget, requiredGear,
                notification, thumbnailUrl, suitableSeasons
        );
    }

    public static AdditionalCategoryInfo of(AdditionalCategoryInfo given) {
        return new AdditionalCategoryInfo(
                given.getBriefInfo(), given.getCategoryTarget(),
                given.getRequiredGear(), given.getWarning(),
                given.getThumbnailUrl(),
                List.of(given.suitableSeasons.toArray(new SuitableSeason[0]))
        );
    }

    public enum SuitableSeason {
        SPRING, SUMMER, AUTUMN, WINTER, ANY
    }
}
