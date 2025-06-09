package org.leisureup.travel.internal.travel.service;

import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.*;
import org.leisureup.travel.internal.travel.repository.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    public ApiResponse<?> createTravel(CreateTravelDto createTravelDto) {
        Travel entity = createTravelDto.toEntity();
        try {
            Travel saved = travelRepository.save(entity);
            return ApiResponse.success(201, "여행 정보가 저장되었습니다.");
        } catch (Exception e) {
            return ApiResponse.failure(500, "저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
