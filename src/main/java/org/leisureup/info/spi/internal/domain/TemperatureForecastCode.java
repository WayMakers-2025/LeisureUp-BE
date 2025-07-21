package org.leisureup.info.spi.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 기상청 중기 예보 API - 중기 기온 예보구역 을 위한 entity
 */
@Getter
@Entity
@DiscriminatorValue("temperature_forecast")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemperatureForecastCode extends RegionCode {

    protected TemperatureForecastCode(String regionCode, String regionName) {
        super(regionCode, regionName);
    }

    public static TemperatureForecastCode of(String regionCode, String regionName) {
        return new TemperatureForecastCode(regionCode, regionName);
    }
}
