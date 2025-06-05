package org.leisureup.travel.internal.travel.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.response.ApiResponse;
import org.leisureup.travel.internal.travel.domain.Travel;
import org.leisureup.travel.internal.travel.dto.CreateTravelDto;
import org.leisureup.travel.internal.travel.repository.TravelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    public ApiResponse<?> createTravel(CreateTravelDto createTravelDto) {
        Travel entity = createTravelDto.toEntity();
        try {
            Travel saved = travelRepository.save(entity);
            return ApiResponse.success("여행 정보가 저장되었습니다.");
        } catch (Exception e) {
            return ApiResponse.failure("저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
