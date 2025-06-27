package org.leisureup.global.auth.social.internal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.*;
import com.fasterxml.jackson.databind.annotation.*;
import java.security.*;
import org.leisureup.global.auth.social.*;

@JsonNaming(SnakeCaseStrategy.class)
public record GetUserInfoKakao(
        String sub,
        String name,
        String nickname,
        String picture,
        String email,
        Boolean emailVerified,
        String gender,
        String birthdate,
        String phoneNumber,
        Boolean phoneNumberVerified
) {

    public OAuthResponse toOAuthResponse() {
        validate();

        String userEmail = null;
        if (
                email != null && !email.isEmpty() &&
                emailVerified != null && emailVerified
        ) {
            userEmail = email;
        }

        return OAuthResponse.of(
                sub,
                name == null || name.isEmpty() ? nickname : name,
                userEmail
        );
    }

    private void validate() {
        if (sub == null) {
            throw new InvalidParameterException("sub is null");
        }

        if ((name == null || name.isEmpty()) && (nickname == null || nickname.isEmpty())) {
            throw new InvalidParameterException("name is empty");
        }
    }
}
