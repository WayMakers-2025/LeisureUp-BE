package org.leisureup.member.internal.dto.response;

import org.leisureup.member.internal.domain.*;

public record GetMemberResponse(
        String nickname,
        String email
) {

    public static GetMemberResponse of(Member member) {
        return new GetMemberResponse(
                member.getNickname(), member.getEmail()
        );
    }
}
