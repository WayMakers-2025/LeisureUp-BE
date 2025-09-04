package org.leisureup.location.internal.domain;

import lombok.*;
import org.leisureup.location.internal.dto.api.*;
import org.springframework.data.annotation.*;
import org.springframework.data.redis.core.*;

@Getter
@RedisHash(value = "fetched-common-info", timeToLive = 120)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonInfoCache {

    @Id
    private Long locationId;
    private CommonInfo info;

    private CommonInfoCache(Long locationId, CommonInfo info) {
        this.locationId = locationId;
        this.info = info;
    }

    public static CommonInfoCache of(Long locationId, CommonInfo info) {
        return new CommonInfoCache(locationId, info);
    }
}
