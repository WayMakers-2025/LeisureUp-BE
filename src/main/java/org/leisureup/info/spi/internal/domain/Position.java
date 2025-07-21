package org.leisureup.info.spi.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 임의의 한 구역 코드에 속하는 좌표 정보
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 위도
    @Column(nullable = false)
    private double gpsX;

    // 경도
    @Column(nullable = false)
    private double gpsY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code_id")
    private RegionCode regionCode;

    public Position(double gpsX, double gpsY) {
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }

    public static Position of(double x, double y) {
        return new Position(x, y);
    }

    public double getDistFrom(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void changeRegionTo(RegionCode regionCode) {
        this.regionCode = regionCode;
    }
}
