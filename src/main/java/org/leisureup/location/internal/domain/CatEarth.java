package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 땅 타입 카테고리
 */
@Getter
@Entity
@DiscriminatorValue(CatEarth.TYPE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatEarth extends Category {

    public static final String TYPE = "EARTH";

    private CatEarth(String name, String categoryCode) {
        super(name, categoryCode);
    }

    public static CatEarth of(String name, String code) {
        return new CatEarth(name, code);
    }

    @Override
    public String getCategoryType() {
        return TYPE;
    }
}
