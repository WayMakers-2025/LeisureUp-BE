package org.leisureup.map.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.global.exception.NotFound;

import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Category {
    restaurant("FD6",39),
    hotel("AD5",32),
    hospital("HP8",null);

    /**
     * 관광지 12
     * 문화시설 14
     * 행사/공연/축제 15
     * 여행코스 25
     * 레포츠 28
     * 숙박 32
     * 쇼핑 38
     * 음식점 39
     */
    private String kakaoCategory;

    private Integer tourContentTypeId;

    public static String toKakaoCategory(String name) {
        try {
            return Category.valueOf(name).getKakaoCategory();
        } catch (IllegalArgumentException e) {
            throw new NotFound("존재하지 않는 카테고리입니다: ");
        }
    }

}
