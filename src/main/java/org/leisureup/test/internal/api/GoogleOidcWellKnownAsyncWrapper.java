package org.leisureup.test.internal.api;

import java.util.concurrent.*;
import lombok.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class GoogleOidcWellKnownAsyncWrapper {

    private final GoogleOidcWellKnown testClient;

    public GoogleWellKnownResp onSync() {
        return testClient.get();
    }

    @Async
    public CompletableFuture<GoogleWellKnownResp> onAsync() {
        // testClient.get() 아니어도 딱히 상관 X
        return CompletableFuture.completedFuture(onSync());
    }
}
