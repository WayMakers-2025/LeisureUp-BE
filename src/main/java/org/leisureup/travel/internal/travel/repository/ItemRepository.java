package org.leisureup.travel.internal.travel.repository;

import org.leisureup.travel.internal.travel.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
