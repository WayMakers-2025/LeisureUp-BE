package org.leisureup.test.internal.api;

import com.fasterxml.jackson.annotation.*;

// 테스트용이니까 없는건 그냥 안넣도록
@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleWellKnownResp(
        String issuer
) {

    private static final String GOOD = "https://accounts.google.com";

    public boolean isValid() {
        return GOOD.equals(issuer);
    }

}
