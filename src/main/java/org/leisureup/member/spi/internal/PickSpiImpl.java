package org.leisureup.member.spi.internal;

import java.util.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class PickSpiImpl implements PickSpi {

    private final MemberRepository memberRepo;
    private final PickRepository pickRepo;

    @Override
    public List<Long> filterPickedLocationFromMember(
            Long memberId, List<Long> locationIds
    ) {

        memberRepo.findById(memberId).orElseThrow(
                () -> new NotFound("Member not found")
        );

        return pickRepo.filterLocationIdsInPick(memberId, locationIds);
    }
}
