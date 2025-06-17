package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Kakao 소셜 인증 정보를 저장할 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOAuth extends BaseTimeEntity {

    @Id
    private String id;

    @OneToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    public static KakaoOAuth of(String socialId, Member member) {
        return new KakaoOAuth(socialId, member);
    }
}
