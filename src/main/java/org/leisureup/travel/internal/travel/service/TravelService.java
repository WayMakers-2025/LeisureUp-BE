package org.leisureup.travel.internal.travel.service;

import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.*;
import org.leisureup.location.spi.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.*;
import org.leisureup.travel.internal.travel.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final LocationQueryPort locationQueryPort;

    public List<GetAllTravelDto> getAllTravel(){
        List<Travel> travels = travelRepository.findAll();
        if (travels.isEmpty()){
            throw new NotFound("생성된 여행이 없습니다.");
        }
        return GetAllTravelDto.fromTravel(travels);
    }

    @Transactional(readOnly = true)
    public GetTravelDetailDto getTravelDetail(Long travelId){
        Travel byId = travelRepository.findById(travelId)
                .orElseThrow(()-> new NotFound("여행이 없습니다."));
        List<Long> locationIdList = new ArrayList<>();

        byId.getItems().stream().forEach((item)->{locationIdList.add(item.getLocationId());});
        List<LocationResponse> locationListById = locationQueryPort.getLocationListById(locationIdList);
        return GetTravelDetailDto.fromEntity(byId, locationListById);
    }

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
