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

    public CatWater(String name, String categoryCode) {
        super(name, categoryCode);
    }

    public static CatWater of(String name, String categoryCode) {
        return new CatWater(name, categoryCode);
    }

    @Override
    public String getCategoryType() {
        return TYPE;
    }
}
