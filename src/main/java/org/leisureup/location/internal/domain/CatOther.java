package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 땅, 하늘, 물 타입 카테고리 외 나머지
 */
@Getter
@Entity
@DiscriminatorValue(CatOther.TYPE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatOther extends Category {

    public static final String TYPE = "OTHER";

    private CatOther(String name, String categoryCode) {
        super(name, categoryCode);
    }

    public static CatOther of(String name, String code) {
        return new CatOther(name, code);
    }

    @Override
    public String getCategoryType() {
        return TYPE;
    }
}
