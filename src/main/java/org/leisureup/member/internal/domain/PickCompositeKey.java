package org.leisureup.member.internal.domain;

import lombok.*;

/**
 * 사용자 찜 목록 저장용 복합키
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PickCompositeKey {

    private Member member;
    private long locationId;
}
