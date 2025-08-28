package org.leisureup.global.auth.dto.api;

import java.util.*;

public record GetAppleOidcOpenKeys(
        List<AppleOidcKey> keys
) {

    public record AppleOidcKey(
            String alg,
            String e,
            String kid,
            String kty,
            String n,
            String use
    ) {

    }
}
