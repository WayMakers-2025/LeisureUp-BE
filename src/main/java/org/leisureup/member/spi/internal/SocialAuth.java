package org.leisureup.member.spi.internal;

import java.util.*;
import org.leisureup.member.spi.*;

public interface SocialAuth {

    Optional<Long> findMemberIdBySocial(Long socialId);

    SocialType getSocialAuthType();
}
