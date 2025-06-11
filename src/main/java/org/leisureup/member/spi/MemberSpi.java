package org.leisureup.member.spi;

import java.util.*;
import org.springframework.transaction.annotation.*;

public interface MemberSpi {

    /**
     * ID 에 해당하는 사용자가 없는지 확인
     */
    boolean notExists(Long memberId);

    /**
     * 주어진 정보로 DB 에 저장된 사용자 ID 를 제공
     */
    Optional<Long> getMemberIdWithSocial(
            SocialType type, Long socialId
    );

    /**
     * 주어진 정보로 새로운 사용자를 생성
     */
    @Transactional
    Long saveNewMember(
            SocialType type, Long socialId,
            String nickname, String email
    );
}
