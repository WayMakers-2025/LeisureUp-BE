package org.leisureup.member.internal.service;

import java.util.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.response.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;
    private final InterestRepository interestRepo;
    private final PickRepository pickRepo;
    private final LocationQueryPort locationQueryPort;
    private final LocationFetchSpi locationFetchSpi;
    private final MemberRemovalService memberRemovalService;

    /**
     * 사용자 정보를 조회한다.
     */
    public GetMemberResponse getMember(Long memberId) {
        Member find = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        boolean hasAnswered = interestRepo.existsById(memberId);

        return GetMemberResponse.of(find, hasAnswered);
    }

    /**
     * 사용자 정보를 수정한다.
     * <p>
     * 현재 {@code nickname} 만 변경 가능.
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequest req) {
        Member find = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        String newNickname = req.nickname();
        find.changeNickname(newNickname);
    }

    /**
     * 사용자를 삭제한다.
     */
    @Transactional
    public void deleteMember(Long memberId) {

        Member target = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        memberRemovalService.removeRelatedPicks(memberId);
        memberRemovalService.removeRelatedInterest(memberId);
        memberRemovalService.removeRelatedOAuths(memberId);

        memberRepo.delete(target);

        memberRemovalService.publishMemberRemovalEvent(memberId);
    }

    /**
     * 사용자 니즈 수집 질문을 저장한다.
     * <p>
     * 이전에 응답했으면 수정해 저장한다.
     */
    @Transactional
    @CacheEvict(
            cacheNames = "recommend-on-member",
            key = "#memberId"
    )
    public void saveInterest(Long memberId, SaveInterestRequest req) {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        InterestInfo savingInfo = InterestInfo.of(req);
        Optional<Interest> optional = interestRepo.findById(memberId);

        if (optional.isPresent()) {
            Interest interest = optional.get();
            interest.setInfo(savingInfo);
            return;
        }

        interestRepo.save(Interest.of(member, savingInfo));
    }


    /**
     * 사용자 찜 장소 목록을 조회한다.
     */
    public PageResponse<PickLocation> getPickLocations(
            Long memberId, PageRequest req
    ) {
        Page<Pick> pickLocations = pickRepo.findAllByMemberId(memberId, req);

        if (pickLocations.isEmpty()) {
            return PageResponse.of(pickLocations, Collections.emptyList());
        }

        List<Long> locationIds = pickLocations.map(Pick::getLocationId)
                .getContent();

        List<PickLocation> contents = locationQueryPort
                .getLocationListById(locationIds).stream()
                .map(PickLocation::of)
                .toList();

        return PageResponse.of(pickLocations, contents);
    }

    /**
     * 어느 장소를 찜 목록에 저장한다.
     */
    @Transactional
    public void savePickLocation(Long memberId, SavePickLocationRequest req) {

        Long locationId = req.locationId();

        if (
                locationQueryPort.notExists(locationId) &&
                !locationFetchSpi.fetchIfLocationExists(locationId)
        ) {
            throw new NotFound("Location not found");
        }

        Member find = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        PickCompositeKey key = new PickCompositeKey(find, locationId);
        if (pickRepo.existsById(key)) {
            throw new DuplicatePickException("Pick already exists");
        }

        Pick newPick = Pick.of(find, locationId);
        pickRepo.save(newPick);
    }

    /**
     * 찜 목록의 어느 장소를 삭제한다.
     */
    @Transactional
    public void deletePickLocation(Long memberId, Long locationId) {

        Member find = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        PickCompositeKey key = new PickCompositeKey(find, locationId);
        Pick pick = pickRepo.findById(key)
                .orElseThrow(() -> new NotFound("Pick not found"));

        pickRepo.delete(pick);
    }
}
