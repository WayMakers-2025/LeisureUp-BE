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
    restaurant("FD6"),
    hotel("AD5"),
    hospital("HP8");

    private String kakaoCategory;

    public static String toKakaoCategory(String name) {
        try {
            return Category.valueOf(name).getKakaoCategory();
        } catch (IllegalArgumentException e) {
            throw new NotFound("존재하지 않는 카테고리입니다: ");
        }
    }

}
