package org.leisureup.global.auth.dto.api;

import java.io.*;
import java.util.*;

public record GetAppleOidcOpenKeys(
        List<AppleOidcKey> keys
) implements Serializable {

    public record AppleOidcKey(
            String alg,
            String e,
            String kid,
            String kty,
            String n,
            String use
    ) implements Serializable {

    }
}
