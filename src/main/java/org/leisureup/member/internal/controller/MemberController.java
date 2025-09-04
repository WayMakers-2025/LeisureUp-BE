package org.leisureup.member.internal.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.global.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.response.*;
import org.leisureup.member.internal.service.*;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

@Validated
@JwtAuthRequired
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthHolder authHolder;

    private static PageRequest toPageRequest(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new BadRequestException("Bad request");
        }

        return PageRequest.of(page, Math.min(size, 100));
    }

    @GetMapping     // 멤버 정보 조회
    public ApiResponse<GetMemberResponse> getMember() {
        Long memberId = authHolder.getMemberId();
        GetMemberResponse resp = memberService.getMember(memberId);
        return ApiResponse.success(200, resp);
    }

    @PatchMapping   // 멤버 정보 수정 (현재 닉네임 변경만)
    public ApiResponse<?> updateMember(
            @Valid @RequestBody UpdateMemberRequest req
    ) {
        Long memberId = authHolder.getMemberId();
        memberService.updateMember(memberId, req);

        return ApiResponse.success(204, null);
    }

    @DeleteMapping  // 멤버 삭제
    public ApiResponse<?> deleteMember() {
        Long memberId = authHolder.getMemberId();
        memberService.deleteMember(memberId);

        return ApiResponse.success(204, null);
    }

    @PostMapping("/interest")       // 니즈 수집 질문 응답 저장
    public ApiResponse<?> saveInterest(
            @Valid @RequestBody
            SaveInterestRequest req
    ) {
        Long memberId = authHolder.getMemberId();
        memberService.saveInterest(memberId, req);

        return ApiResponse.success(204, null);
    }


    @GetMapping("/picks")       // 찜 목록 조회
    public ApiResponse<PageResponse<PickLocation>> getPickLocations(
            @PositiveOrZero(message = "page 는 0 보다 크거나 같아야 합니다.")
            @RequestParam(value = "page", defaultValue = "0")
            int page,
            @Positive(message = "size 는 0 보다 커야합니다.")
            @RequestParam(value = "size", defaultValue = "10")
            int size
    ) {
        PageRequest req = toPageRequest(page, size);
        Long memberId = authHolder.getMemberId();

        var resp = memberService.getPickLocations(memberId, req);
        return ApiResponse.success(200, resp);
    }


    @PostMapping("/picks")      // 장소 찜 저장
    public ApiResponse<?> savePickLocation(
            @Valid @RequestBody SavePickLocationRequest req
    ) {
        Long memberId = authHolder.getMemberId();
        memberService.savePickLocation(memberId, req);

        return ApiResponse.success(201, null);
    }


    @DeleteMapping("/picks/{locationId}")       // 찜 장소 삭제
    public ApiResponse<?> deletePickLocation(
            @NotNull(message = "locationId 는 필수입니다.")
            @Positive(message = "locationId 는 0 보다 커야합니다.")
            @PathVariable Long locationId
    ) {
        Long memberId = authHolder.getMemberId();
        memberService.deletePickLocation(memberId, locationId);

        return ApiResponse.success(204, null);
    }
}
