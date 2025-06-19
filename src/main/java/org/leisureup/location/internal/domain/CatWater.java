package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 물 타입 카테고리
 */
@Getter
@Entity
@DiscriminatorValue(CatWater.TYPE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatWater extends Category {

    public static final String TYPE = "WATER";

    private CatWater(String name, String categoryCode) {
        super(name, categoryCode);
    }

    private CatWater(String name, String categoryCode, String recommendationCode) {
        super(name, categoryCode, recommendationCode);
    }

    public static CatWater of(String name, String categoryCode) {
        return new CatWater(name, categoryCode);
    }

    public static CatWater of(String name, String categoryCode, String recommendCode) {
        return new CatWater(name, categoryCode, recommendCode);
    }

    @Override
    public String getCategoryType() {
        return TYPE;
    }
}
