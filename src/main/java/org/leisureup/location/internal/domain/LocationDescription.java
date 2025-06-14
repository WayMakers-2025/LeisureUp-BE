package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 어느 장소에 대한 세부 정보
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationDescription {

    /**
     * 장소 설명글
     *
     * <pre>
     * {@code
     * Ex) 동촌유원지는 대구시 동쪽 금호강변에 있는 44만 평의 유원지로 ...
     * }
     * </pre>
     */
    @Lob
    private String overview;

    /**
     * 장소 홈페이지 url
     *
     * @warning {@code TourApi} 에서 해당 정보는 {@code <a href="...">} 와 같은 태그로 제공됨. 그래서 파싱 제대로 안하면 이상할수도
     * 있음.
     */
    @Lob
    private String homepageInfo;

    @Column(length = 30)
    private String telephoneNumber;

    /**
     * 500 x 300 크기 썸네일 이미지
     */
    @Column(length = 150)
    private String largeThumbnailUrl;

    /**
     * 150 x 100 크기 썸네일 이미지
     */
    @Column(length = 150)
    private String smallThumbnailUrl;

    public static LocationDescription of(
            String overview, String homepageInfo, String telephoneNumber,
            String largeThumbnailUrl, String smallThumbnailUrl
    ) {
        return new LocationDescription(
                overview, homepageInfo, telephoneNumber, largeThumbnailUrl, smallThumbnailUrl
        );
    }

    public static LocationDescription of(LocationDescription desc) {
        return of(
                desc.getOverview(), desc.getHomepageInfo(), desc.getTelephoneNumber(),
                desc.getLargeThumbnailUrl(), desc.getSmallThumbnailUrl()
        );
    }
}
