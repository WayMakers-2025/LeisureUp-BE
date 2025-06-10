package org.leisureup.member.spi;

import java.util.*;

public interface MemberSpi {

    boolean notExists(Long memberId);

    Optional<Long> getMemberIdWithSocial(
            SocialType type, Long socialId
    );
}
