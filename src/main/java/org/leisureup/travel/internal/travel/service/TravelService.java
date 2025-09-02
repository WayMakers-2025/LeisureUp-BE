package org.leisureup.travel.internal.travel.service;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.logging.*;
import org.leisureup.global.response.*;
import org.leisureup.location.spi.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.request.*;
import org.leisureup.travel.internal.travel.dto.response.*;
import org.leisureup.travel.internal.travel.repository.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@LogMethodIO
@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ItemRepository itemRepository;
    private final LocationQueryPort locationQueryPort;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<GetAllTravelResponse> getAllTravel(Long memberId) {
        List<Travel> travels = travelRepository.findByMemberId((memberId));
        if (travels.isEmpty()) {
            throw new NotFound("생성된 여행이 없습니다.");
        }
        Map<Long, String> representImageMap = new HashMap<>();
        for (Travel travel : travels) {
            List<Long> itemIdList = itemRepository.findByTravel(travel).stream()
                    .map(Item::getLocationId)
                    .collect(Collectors.toList());
            String representImage = locationQueryPort.getRepresentImage(itemIdList);
            representImageMap.put(travel.getTravelId(), representImage);
        }
        return GetAllTravelResponse.fromTravel(travels, representImageMap);
    }

    @Transactional(readOnly = true)
    public GetTravelDetailResponse getTravelDetail(Long travelId, Long memberId) {
        Travel travel = this.findTravel(travelId, memberId);

        if (!memberId.equals(travel.getMemberId())) {
            throw new RequestForbiddenException("자신의 여행만 조회할 수 있습니다.");
        }

        List<Item> items = travel.getItems();
        List<Long> locationIds = items.stream().map(Item::getLocationId).toList();
        // 대표 이미지 불러오기
        String representImage = locationQueryPort.getRepresentImage(locationIds);

        // ID : Item mapping
        Map<Long, Item> itemMap = listToMap(travel.getItems(), Item::getLocationId);

        // 필요한 장소 목록들 가져온 후 ID : Resp mapping
        List<LocationResponse> resp = locationQueryPort.getLocationListById(
                new ArrayList<>(itemMap.keySet())
        );
        Map<Long, LocationResponse> locationInfoMap = listToMap(resp, LocationResponse::locationId);

        // 응답 만들기
        List<LocationResponseDetail> detailList = new ArrayList<>();
        for (Long id : locationInfoMap.keySet()) {
            var info = locationInfoMap.get(id);
            var item = itemMap.get(id);
            var detail = LocationResponseDetail.builder()
                    .locationResponse(info)
                    .position(item.getPosition())
                    .startTime(item.getStartTime())
                    .endTime(item.getEndTime())
                    .date(item.getDate())
                    .build();
            detailList.add(detail);
        }
        detailList.sort(java.util.Comparator.comparing(LocationResponseDetail::getPosition));

        return GetTravelDetailResponse.fromEntity(travel, representImage, detailList);
    }

    private static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyMapper) {
        return list.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    @Transactional
    public String addItem(Long travelId, Long locationId, Long memberId) {
        Travel travel = this.findTravel(travelId, memberId);

        int position = travel.getItems().size();
        Item newItem = Item.addItem(locationId, position, travel);
        itemRepository.save(newItem);

        eventPublisher.publishEvent(new FetchLocationEvent(locationId));

        return "성공적으로 아이템이 추가되었습니다.";
    }

    private Travel findTravel(Long travelId, Long memberId) {
        return travelRepository.findByTravelIdAndMemberId(travelId, memberId)
                .orElseThrow(() -> new NotFound("여행이 없습니다."));
    }

    @Transactional
    public String deleteTravel(Long travelId, Long memberId) {

        Travel find = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFound("여행이 없습니다."));

        if (!memberId.equals(find.getMemberId())) {
            throw new RequestForbiddenException("자신의 여행만 삭제할 수 있습니다.");
        }

        itemRepository.deleteAllByTravelId(travelId);
        travelRepository.deleteById(travelId);

        return "성공적으로 삭제되었습니다.";
    }

    @Transactional
    public ApiResponse<String> createTravel(CreateTravelRequest createTravelRequest,
            Long memberId) {
        try {
            // 1. Travel 엔티티 생성 및 저장
            Travel travel = createTravelRequest.toEntity(memberId);
            Travel savedTravel = travelRepository.save(travel);
            
            // 2. Item 엔티티들 생성 및 저장
            if (createTravelRequest.getItems() != null && !createTravelRequest.getItems()
                    .isEmpty()) {
                List<Item> items = createItemsFromRequest(createTravelRequest.getItems(),
                        savedTravel);
                itemRepository.saveAll(items);
                
                // 3. Travel 엔티티에 Item들 연결
                savedTravel.getItems().addAll(items);

                items.stream().map(Item::getLocationId)
                        .map(FetchLocationEvent::new)
                        .forEach(eventPublisher::publishEvent);
            }
            
            return ApiResponse.success(201, "여행 정보가 성공적으로 저장되었습니다.");
            
        } catch (Exception e) {
            return ApiResponse.failure(500, "여행 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * ItemRequest 리스트를 Item 엔티티 리스트로 변환 position이 null인 경우 자동으로 순차적으로 할당
     */
    private List<Item> createItemsFromRequest(List<ItemRequest> itemRequests, Travel travel) {
        List<Item> items = new ArrayList<>();
        
        for (int i = 0; i < itemRequests.size(); i++) {
            ItemRequest itemRequest = itemRequests.get(i);
            
            // position이 null이면 인덱스 기반으로 자동 할당
            int position = itemRequest.getPosition() != null ? itemRequest.getPosition() : i;
            
            Item item = Item.buildItem(itemRequest, position, travel);
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

    @Transactional
    public ApiResponse<String> updateTravel(Long travelId, CreateTravelRequest updateTravelRequest,
            Long memberId) {
        try {
            Travel travel = this.findTravel(travelId, memberId);
            travel.updateTravelInfo(updateTravelRequest);

            if (updateTravelRequest.getItems() != null) {

                List<ItemRequest> reqItems = updateTravelRequest.getItems();

                Map<Long, Item> currentByLocation = travel.getItems().stream()
                        .collect(Collectors.toMap(Item::getLocationId, Function.identity()));

                Set<Long> requestedLocationIds = reqItems.stream()
                        .map(ItemRequest::getLocationId)
                        .collect(Collectors.toSet());

                // 1) 삭제: 요청에 없는 항목 제거
                List<Item> toRemove = travel.getItems().stream()
                        .filter(existing -> !requestedLocationIds.contains(existing.getLocationId()))
                        .toList();
                if (!toRemove.isEmpty()) {
                    toRemove.forEach(itemRepository::delete);
                    travel.getItems().removeAll(toRemove);
                }

                // 2) 추가/업데이트
                for (int i = 0; i < reqItems.size(); i++) {
                    ItemRequest req = reqItems.get(i);
                    Item existing = currentByLocation.get(req.getLocationId());
                    int desiredPosition = req.getPosition() != null ? req.getPosition() : i;

                    if (existing == null) {
                        Item created = Item.buildItem(req, desiredPosition, travel);
                        itemRepository.save(created);
                        travel.getItems().add(created);
                    } else {
                        existing.updatePosition(desiredPosition);
                        existing.updateDate(req.getDate());
                        existing.updateTime(req.getStartTime(), req.getEndTime());
                    }
                }

                // 3) 포지션 정규화: position 기준 정렬 후 0..n-1 재부여
                travel.getItems().sort(Comparator.comparingInt(Item::getPosition));
                for (int i = 0; i < travel.getItems().size(); i++) {
                    travel.getItems().get(i).updatePosition(i);
                }

                // 4) 요청 locationId 들로 이벤트 발행
                requestedLocationIds.stream()
                        .distinct()
                        .map(FetchLocationEvent::new)
                        .forEach(eventPublisher::publishEvent);
            }
            travelRepository.save(travel);
            
            return ApiResponse.success(200, "여행 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ApiResponse.failure(500, "여행 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
