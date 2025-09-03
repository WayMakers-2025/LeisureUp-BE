package org.leisureup.travel.internal.travel.controller;

import java.util.*;
import lombok.*;
import org.leisureup.global.*;
import org.leisureup.global.logging.*;
import org.leisureup.global.response.*;
import org.leisureup.travel.internal.travel.dto.request.*;
import org.leisureup.travel.internal.travel.dto.response.*;
import org.leisureup.travel.internal.travel.service.*;
import org.springframework.web.bind.annotation.*;

@LogMethodInvocation
@RestController
@RequiredArgsConstructor
@JwtAuthRequired
public class TravelController {

    private final TravelService travelService;
    private final AuthHolder authHolder;


    /**
     * 1) 찜 목록에서 + 를 누른 경우
     * 2) 하단의 경로 탭을 누른 경우
     */
    @GetMapping("/travels")
    public ApiResponse<List<GetAllTravelResponse>> getAllTravel(){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.getAllTravel(memberId)
        );
    }

    /**
     * 여행 1개 상세 조회
     */
    @GetMapping("/travels/{travelId}")
    public ApiResponse<GetTravelDetailResponse> getTravelDetail(@PathVariable Long travelId){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.getTravelDetail(travelId,memberId)
        );
    }
    /**
     * 여행에 Item 하나 추가
     */
    @PostMapping("/travels/{travelId}")
    public ApiResponse<String> addItem(@PathVariable Long travelId,
                                  AddItemRequest addItemRequest){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.addItem(travelId, addItemRequest.getLocationId(), memberId)
        );
    }

    /**
     * 여행 삭제
     */
    @DeleteMapping("/travels/{travelId}")
    public ApiResponse<String> deleteTravel(@PathVariable Long travelId){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.deleteTravel(travelId, memberId)
        );
    }

    /**
     * 여행 생성
     */
    @PostMapping("/travels")
    public ApiResponse<String> createTravel(@RequestBody CreateTravelRequest createTravelRequest) {
        Long memberId = authHolder.getMemberId();
        return travelService.createTravel(createTravelRequest, memberId);
    }

    /**
     * 여행의 Item 하나 삭제
     */
    @DeleteMapping("/travels/{travelId}/{itemId}")
    public ApiResponse<String> deleteItem(@PathVariable Long travelId, @PathVariable Long itemId) {
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(200, travelService.deleteItem(travelId, itemId, memberId));
    }

    /**
     * 여행 정보 수정
     */
    @PutMapping("/travels/{travelId}")
    public ApiResponse<String> updateTravel(@PathVariable Long travelId, 
                                           @RequestBody CreateTravelRequest updateTravelRequest) {
        Long memberId = authHolder.getMemberId();
        return travelService.updateTravel(travelId, updateTravelRequest, memberId);
    }
}
