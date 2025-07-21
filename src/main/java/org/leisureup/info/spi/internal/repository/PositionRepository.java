package org.leisureup.info.spi.internal.repository;


import java.util.*;
import org.leisureup.info.spi.internal.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface PositionRepository extends JpaRepository<Position, Long> {

    /**
     * {@link LandForecastCode} 에 속하는 좌표 중 가까운 순으로 조회
     */
    @Query("""
            select p from Position p
            inner join fetch p.regionCode rc
            where type(rc) = LandForecastCode
            order by sqrt(
                power(abs(p.gpsX - :x),2) +
                power(abs(p.gpsY - :y),2)
            )
            """)
    List<Position> findCloseLandForecastPositions(
            double x, double y, Pageable pageable
    );

    /**
     * {@link TemperatureForecastCode} 에 속하는 좌표 중 가까운 순으로 조회
     */
    @Query("""
            select p from Position p
            inner join fetch p.regionCode rc
            where type(rc) = TemperatureForecastCode
            order by sqrt(
                power(abs(p.gpsX - :x),2) +
                power(abs(p.gpsY - :y),2)
            )
            """)
    List<Position> findCloseTemperatureForecastPositions(
            double x, double y, Pageable pageable
    );
}
