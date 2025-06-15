package org.leisureup.travel.internal.travel.controller;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.AuthHolder;
import org.leisureup.global.JwtAuthRequired;
import org.leisureup.global.response.ApiResponse;
import org.leisureup.travel.internal.travel.dto.CreateTravelDto;
import org.leisureup.travel.internal.travel.dto.GetAllTravelDto;
import org.leisureup.travel.internal.travel.dto.GetTravelDetailDto;
import org.leisureup.travel.internal.travel.service.TravelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<List<GetAllTravelDto>> getAllTravel(){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.getAllTravel(memberId)
        );
    }

    @GetMapping("/travels/{travelId}")
    public ApiResponse<GetTravelDetailDto> getTravelDetail(@PathVariable Long travelId){
        Long memberId = authHolder.getMemberId();
        return ApiResponse.success(
                200,
                travelService.getTravelDetail(travelId,memberId)
        );
    }

    public ApiResponse<?> createTravel(CreateTravelDto createTravelDto) {
        return travelService.createTravel(createTravelDto);
    }
}
