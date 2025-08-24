package org.leisureup.info.recommend.controller;

import java.util.*;
import lombok.*;
import org.leisureup.global.*;
import org.leisureup.global.response.*;
import org.leisureup.info.recommend.dto.response.*;
import org.leisureup.info.recommend.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommends")
@RequiredArgsConstructor
public class RecommendController {

    private final AuthHolder authHolder;
    private final RecommendService recommendService;

    @JwtAuthIfPossible
    @GetMapping("/member")  // 사용자 질문 응답 기반 레조 (카테고리) 종류를 추천
    public ApiResponse<List<CategoryRecommendation>> recommendOnMember() {

        Long memberId = authHolder.getMemberId();

        return ApiResponse.success(
                200, memberId == null ?
                        recommendService.recommendOnAnonymous() :
                        recommendService.recommendOnMember(memberId)
        );
    }

    @GetMapping("/season")  // 계절에 맞는 레저 장소를 추천
    public ApiResponse<List<LocationRecommendation>> recommendOnSeason() {

        var resp = recommendService.recommendOnSeason();

        return ApiResponse.success(200, resp);
    }
}
