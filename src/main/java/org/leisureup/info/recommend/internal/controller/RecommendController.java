package org.leisureup.info.recommend.internal.controller;

import java.util.*;
import lombok.*;
import org.leisureup.global.*;
import org.leisureup.global.response.*;
import org.leisureup.info.recommend.internal.dto.response.*;
import org.leisureup.info.recommend.internal.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommends")
@RequiredArgsConstructor
public class RecommendController {

    private final AuthHolder authHolder;
    private final RecommendService recommendService;

    @JwtAuthIfPossible
    @GetMapping("/member")  // 사용자 질문 응답 기반 장소 추천
    public ApiResponse<List<LocationInfo>> recommendOnMember() {

        Long memberId = authHolder.getMemberId();

        return ApiResponse.success(
                200, memberId == null ?
                        recommendService.recommendOnAnonymous() :
                        recommendService.recommendOnMember(memberId)
        );
    }
}
