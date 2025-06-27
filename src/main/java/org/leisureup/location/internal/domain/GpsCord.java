package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * GPS 경도 (x), 위도 (y) 좌표
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GpsCord {

    /**
     * 경도
     */
    @Column(nullable = false)
    private double gpsX;

    /**
     * 위도
     */
    @Column(nullable = false)
    private double gpsY;

    public static GpsCord of(double x, double y) {
        return new GpsCord(x, y);
    }

    public static GpsCord of(GpsCord cord) {
        return new GpsCord(cord.getGpsX(), cord.getGpsY());
    }
}
