package org.leisureup.travel.internal.travel.service;

import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.*;
import org.leisureup.location.spi.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.request.CreateTravelRequest;
import org.leisureup.travel.internal.travel.dto.response.GetAllTravelResponse;
import org.leisureup.travel.internal.travel.dto.response.GetTravelDetailResponse;
import org.leisureup.travel.internal.travel.dto.response.LocationResponseDetail;
import org.leisureup.travel.internal.travel.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ItemRepository itemRepository;
    private final LocationQueryPort locationQueryPort;

    @Transactional(readOnly = true)
    public List<GetAllTravelResponse> getAllTravel(Long memberId){
        List<Travel> travels = travelRepository.findByMemberId((memberId));
        if (travels.isEmpty()){
            throw new NotFound("생성된 여행이 없습니다.");
        }
        return GetAllTravelResponse.fromTravel(travels);
    }

    @Transactional(readOnly = true)
    public GetTravelDetailResponse getTravelDetail(Long travelId, Long memberId){
        Travel travel = this.findTravel(travelId, memberId);
        List<Long> locationIdList = new ArrayList<>();

//        travel.getItems().stream().forEach((item)->{locationIdList.add(item.getLocationId());});
        List<Item> sortedItems = travel.getItems().stream()
                .sorted(Comparator.comparing(Item::getPosition))
                .toList();

        // 정렬된 Item에서 locationId 추출
        sortedItems.forEach(item -> locationIdList.add(item.getLocationId()));

        List<LocationResponse> locationListById = locationQueryPort.getLocationListById(locationIdList);
        List<LocationResponseDetail> locationResponseDetailList = new ArrayList<>();
        for(int i=0; i<sortedItems.size(); i++){
            Item item = sortedItems.get(i);
            LocationResponse locationResponse = locationListById.get(i);
            LocationResponseDetail locationResponseDetail =
                    new LocationResponseDetail(locationResponse, item.getPosition());
            locationResponseDetailList.add(locationResponseDetail);
        }
        return GetTravelDetailResponse.fromEntity(travel, locationResponseDetailList);
    }

    public String addItem(Long travelId, Long locationId, Long memberId){
        Travel travel = this.findTravel(travelId, memberId);
        if(travel.getItems().isEmpty()){
            Item.buildItem(locationId,0, travel);
        }
        int position = travel.getItems().stream()
                .max(Comparator.comparing(Item::getPosition)).get().getPosition();
        itemRepository.save(Item.buildItem(locationId,position+1,travel));
        return "성공적으로 처리되었습니다.";
    }

    private Travel findTravel(Long travelId, Long memberId){
        return travelRepository.findByTravelIdAndMemberId(travelId,memberId)
                .orElseThrow(()-> new NotFound("여행이 없습니다."));
    }

    public ApiResponse<?> createTravel(CreateTravelRequest createTravelRequest) {
        Travel entity = createTravelRequest.toEntity();
        try {
            Travel saved = travelRepository.save(entity);
            return ApiResponse.success(201, "여행 정보가 저장되었습니다.");
        } catch (Exception e) {
            return ApiResponse.failure(500, "저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
