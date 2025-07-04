package org.leisureup.member.internal.dto.response;

import org.leisureup.member.internal.domain.*;

public record GetMemberResponse(
        String nickname,
        String email,
        boolean hasAnswered
) {

    public static GetMemberResponse of(Member member, boolean hasAnswered) {
        return new GetMemberResponse(
                member.getNickname(), member.getEmail(),
                hasAnswered
        );
    }
}
