package org.leisureup.test.internal.api;

import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "GoogleOidcWellKnown",
        url = "https://accounts.google.com"
)
public interface GoogleOidcWellKnown {

    @GetMapping("/.well-known/openid-configuration")
    GoogleWellKnownResp get();
}
