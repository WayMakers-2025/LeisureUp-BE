package org.leisureup.travel.internal.travel.service;

import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.*;
import org.leisureup.location.spi.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.request.CreateTravelRequest;
import org.leisureup.travel.internal.travel.dto.request.ItemRequest;
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

    @Transactional
    public String addItem(Long travelId, Long locationId, Long memberId){
        Travel travel = this.findTravel(travelId, memberId);
        
        // position 계산: 기존 아이템이 없으면 0, 있으면 최대값 + 1
        int position = travel.getItems().isEmpty() ? 0 : 
                travel.getItems().stream()
                        .mapToInt(Item::getPosition)
                        .max()
                        .orElse(0) + 1;
        
        Item newItem = Item.buildItem(locationId, position, travel);
        itemRepository.save(newItem);
        
        return "성공적으로 아이템이 추가되었습니다.";
    }

    private Travel findTravel(Long travelId, Long memberId){
        return travelRepository.findByTravelIdAndMemberId(travelId,memberId)
                .orElseThrow(()-> new NotFound("여행이 없습니다."));
    }

    public String deleteTravel(Long travelId, Long memberId){
        try{
            travelRepository.deleteByTravelIdAndMemberId(travelId, memberId);
        } catch (Exception e){
            throw new NotFound("여행이 없습니다.");
        }
        return "성공적으로 삭제되었습니다.";
    }

    @Transactional
    public ApiResponse<String> createTravel(CreateTravelRequest createTravelRequest, Long memberId) {
        try {
            // 1. Travel 엔티티 생성 및 저장
            Travel travel = createTravelRequest.toEntity(memberId);
            Travel savedTravel = travelRepository.save(travel);
            
            // 2. Item 엔티티들 생성 및 저장
            if (createTravelRequest.getItems() != null && !createTravelRequest.getItems().isEmpty()) {
                List<Item> items = createItemsFromRequest(createTravelRequest.getItems(), savedTravel);
                itemRepository.saveAll(items);
                
                // 3. Travel 엔티티에 Item들 연결
                savedTravel.getItems().addAll(items);
            }
            
            return ApiResponse.success(201, "여행 정보가 성공적으로 저장되었습니다.");
            
        } catch (Exception e) {
            return ApiResponse.failure(500, "여행 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * ItemRequest 리스트를 Item 엔티티 리스트로 변환
     * position이 null인 경우 자동으로 순차적으로 할당
     */
    private List<Item> createItemsFromRequest(List<ItemRequest> itemRequests, Travel travel) {
        List<Item> items = new ArrayList<>();
        
        for (int i = 0; i < itemRequests.size(); i++) {
            ItemRequest itemRequest = itemRequests.get(i);
            
            // position이 null이면 인덱스 기반으로 자동 할당
            int position = itemRequest.getPosition() != null ? itemRequest.getPosition() : i;
            
            Item item = Item.buildItem(itemRequest.getLocationId(), position, travel);
            items.add(item);
        }
        
        return items;
    }

}
