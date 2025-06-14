package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Google 소셜 인증 정보를 저장할 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleOAuth extends BaseTimeEntity {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    public static GoogleOAuth of(Long socialId, Member member) {
        return new GoogleOAuth(socialId, member);
    }
}
