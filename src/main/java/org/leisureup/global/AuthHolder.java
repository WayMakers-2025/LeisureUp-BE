package org.leisureup.global;

import lombok.*;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.annotation.*;

@Component
@RequiredArgsConstructor
public class AuthHolder {

    private final ObjectProvider<InternalAuth> authProvider;

    /**
     * 인증된 사용자 ID 를 제공한다.
     *
     * @warning Jwt 를 통해 인증이 실패한 경우 반환값이 {@code null} 임에 유의.
     */
    public Long getMemberId() {
        return authProvider.getObject().getId();
    }

    /**
     * 인증된 사용자 ID 를 저장한다.
     */
    public void setMemberId(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member id cannot be null");
        }

        Long result = authProvider.getObject()
                .setId(memberId);

        if (!memberId.equals(result)) {
            throw new IllegalArgumentException(
                    "Authentication has already been set"
            );
        }
    }
}

@Component
@RequestScope
class InternalAuth {

    private Long memberId;

    public Long setId(Long id) {
        return memberId != null ?
                memberId : (memberId = id);
    }

    public Long getId() {
        return memberId;
    }
}
