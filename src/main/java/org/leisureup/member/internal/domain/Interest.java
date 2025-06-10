package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 니즈 수집 질문을 저장하는 엔티티
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {

    @Id
    private Long memberId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Embedded
    private InterestInfo info;

}
