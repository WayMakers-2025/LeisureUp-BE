package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 하늘 타입 카테고리
 */
@Getter
@Entity
@DiscriminatorValue(CatSky.TYPE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatSky extends Category {

    public static final String TYPE = "SKY";

    private CatSky(String name, String categoryCode) {
        super(name, categoryCode);
    }

    public static CatSky of(String name, String categoryCode) {
        return new CatSky(name, categoryCode);
    }

    @Override
    public String getCategoryType() {
        return TYPE;
    }
}
