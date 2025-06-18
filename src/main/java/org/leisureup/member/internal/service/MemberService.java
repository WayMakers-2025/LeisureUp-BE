package org.leisureup.member.internal.service;

import jakarta.transaction.*;
import java.util.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.response.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;
    private final InterestRepository interestRepo;
    private final PickRepository pickRepo;
    private final LocationQueryPort locationQueryPort;

    /**
     * 사용자 정보를 조회한다.
     */
    public GetMemberResponse getMember(Long memberId) {
        Member find = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFound("Member not found"));

        return GetMemberResponse.of(find);
    }

    /**
     * 사용자 니즈 수집 질문을 저장한다.
     * <p>
     * 이전에 응답했으면 수정해 저장한다.
     */
    @Transactional
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

}
