package org.leisureup.travel.internal.travel.repository;

import org.leisureup.travel.internal.travel.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findByMemberId(Long memberId);

    Optional<Travel> findByTravelIdAndMemberId(Long travelId, Long memberId);

    void deleteByTravelIdAndMemberId(Long travelId, Long memberId);
}
