package org.leisureup.info.spi.internal.repository;

import java.util.*;
import org.leisureup.info.spi.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface RegionCodeRepository extends JpaRepository<RegionCode, Long> {

    Optional<RegionCode> findByRegionCode(String regionCode);

}
