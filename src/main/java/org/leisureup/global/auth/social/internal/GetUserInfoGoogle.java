package org.leisureup.global.auth.social.internal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.*;
import com.fasterxml.jackson.databind.annotation.*;
import java.security.*;
import org.leisureup.global.auth.social.*;

@JsonNaming(SnakeCaseStrategy.class)
public record GetUserInfoGoogle(
        String sub,
        String name,
        String givenName,
        String familyName,
        String picture,
        String email,
        Boolean emailVerified
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
                sub, name, userEmail
        );
    }

    private void validate() {
        if (sub == null) {
            throw new InvalidParameterException("sub is null");
        }

        if ((name == null || name.isEmpty())) {
            throw new InvalidParameterException("name is empty");
        }
    }
}
