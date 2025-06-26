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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public GetTravelDetailResponse getTravelDetail(Long travelId, Long memberId) {
        Travel travel = this.findTravel(travelId, memberId);

        // ID : Item mapping
        Map<Long, Item> itemMap = listToMap(travel.getItems(), Item::getLocationId);

        // 필요한 장소 목록들 가져온 후 ID : Resp mapping
        var resp = locationQueryPort.getLocationListById(
                new ArrayList<>(itemMap.keySet())
        );
        Map<Long, LocationResponse> locationInfoMap = listToMap(resp, LocationResponse::locationId);

        // 응답 만들기
        List<LocationResponseDetail> detailList = new ArrayList<>();
        for (Long id : locationInfoMap.keySet()) {
            var info = locationInfoMap.get(id);
            var item = itemMap.get(id);
            var detail = new LocationResponseDetail(info, item.getPosition());
            detailList.add(detail);
        }
        detailList.sort(Comparator.comparing(LocationResponseDetail::getPosition));

        return GetTravelDetailResponse.fromEntity(travel, detailList);
    }

    private static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyMapper) {
        return list.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    @Transactional
    public String addItem(Long travelId, Long locationId, Long memberId){
        Travel travel = this.findTravel(travelId, memberId);

        int position = travel.getItems().size();
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

    @Transactional
    public String deleteItem(Long travelId, Long itemId, Long memberId) {
        Travel travel = this.findTravel(travelId, memberId);

        Item itemToDelete = travel.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFound("해당 여행에 존재하지 않습니다."));

        int deletedPosition = itemToDelete.getPosition();

        itemRepository.delete(itemToDelete);
        travel.getItems().stream()
                .filter(item -> !item.getItemId().equals(itemId))
                .filter(item -> item.getPosition() > deletedPosition)
                .forEach(item -> item.updatePosition(item.getPosition() - 1));

        return "성공적으로 삭제되었습니다.";
    }
}
