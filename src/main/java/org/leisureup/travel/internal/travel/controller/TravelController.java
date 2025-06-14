package org.leisureup.travel.internal.travel.controller;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.response.ApiResponse;
import org.leisureup.travel.internal.travel.dto.CreateTravelDto;
import org.leisureup.travel.internal.travel.dto.GetAllTravelDto;
import org.leisureup.travel.internal.travel.dto.GetTravelDetailDto;
import org.leisureup.travel.internal.travel.service.TravelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    /**
     * 1) 찜 목록에서 + 를 누른 경우
     * 2) 하단의 경로 탭을 누른 경우
     */
    // TODO 사용자 기반 조회
    @GetMapping("/travels")
    public ApiResponse<List<GetAllTravelDto>> getAllTravel(){
        return ApiResponse.success(
                200,
                travelService.getAllTravel()
        );
    }

    @GetMapping("/travels/{travelId}")
    public ApiResponse<GetTravelDetailDto> getTravelDetail(@PathVariable Long travelId){
        return ApiResponse.success(
                200,
                travelService.getTravelDetail(travelId)
        );
    }

    public ApiResponse<?> createTravel(CreateTravelDto createTravelDto) {
        return travelService.createTravel(createTravelDto);
    }
}
