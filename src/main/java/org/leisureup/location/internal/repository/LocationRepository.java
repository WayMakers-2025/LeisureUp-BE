package org.leisureup.location.internal.repository;

import org.leisureup.location.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
