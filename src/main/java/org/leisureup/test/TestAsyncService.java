package org.leisureup.test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import org.leisureup.test.internal.api.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TestAsyncService {

    private final GoogleOidcWellKnownAsyncWrapper asyncWrapper;

    public List<GoogleWellKnownResp> testSync(int testSize) {
        return IntStream.range(0, testSize)
                .mapToObj(i -> asyncWrapper.onSync())
                .toList();
    }

    public List<GoogleWellKnownResp> testAsync(int testSize) {

        // 별도의 스레드에서 asyncWrapper.onAsync() 가 수행 (하도록 q 넣음)
        List<CompletableFuture<GoogleWellKnownResp>> completableFutures
                = IntStream.range(0, testSize)
                .mapToObj(i -> asyncWrapper.onAsync())
                .toList();

        // 모든 작업이 끝날때까지 기다림 (join)
        // 수행한 내역의 결과를 반환
        return completableFutures.stream()
                .map(CompletableFuture::join)   // 만약 수행 중 에러가 발생했었면 여기서 해당 에러가 다시 thrown
                .toList();
    }
}
