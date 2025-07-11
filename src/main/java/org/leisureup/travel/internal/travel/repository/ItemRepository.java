package org.leisureup.travel.internal.travel.repository;

import org.leisureup.travel.internal.travel.domain.Item;
import org.leisureup.travel.internal.travel.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTravel(Travel travel);
}
