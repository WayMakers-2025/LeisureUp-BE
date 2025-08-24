package org.leisureup;

import lombok.*;
import org.leisureup.global.*;
import org.leisureup.global.response.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class JwtAuthTestController {

    // Jwt 인가 테스트용 controller

    private final AuthHolder authHolder;

    @JwtAuthRequired
    @GetMapping("/auth-required")
    public ApiResponse<Long> getMemberIdFromJwt() {
        Long id = authHolder.getMemberId();
        return ApiResponse.success(200, id);
    }

    @JwtAuthIfPossible
    @GetMapping("/auth-bypassable")
    public ApiResponse<Long> getMemberFromJwt() {
        Long id = authHolder.getMemberId();
        return ApiResponse.success(200, id);
    }
}
