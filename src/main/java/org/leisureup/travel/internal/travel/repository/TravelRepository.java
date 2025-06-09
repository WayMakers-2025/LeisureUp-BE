package org.leisureup.travel.internal.travel.repository;

import org.leisureup.travel.internal.travel.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}
