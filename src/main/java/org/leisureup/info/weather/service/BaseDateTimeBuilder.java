package org.leisureup.info.weather.service;

import java.time.*;
import java.time.format.*;
import java.util.*;
import org.leisureup.info.weather.dto.*;
import org.springframework.stereotype.*;

/**
 * 단기 예보 API 의 기준 날짜, 시각을 제공하는 {@code component}
 */
@Component
public class BaseDateTimeBuilder {

    private static final DateTimeFormatter baseDateFormatter
            = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter timeFormatter
            = DateTimeFormatter.ofPattern("HHmm");
    private static final String[] BASE_TIMES = {
            "0200", "0500", "0800", "1100",
            "1400", "1700", "2000", "2300"
    };
    private static final String[] BASE_TIME_THRESHOLDS = {
            "0215", "0515", "0815", "1115",
            "1415", "1715", "2015", "2315"
    };

    /**
     * 주어진 시각에서 단기 예보 시각 {@code (baseDate, baseTime)} 을 구성해 제공한다.
     */
    public ShortTermBaseDateTimeInfo buildInfoFrom(LocalDateTime time) {

        String currentTime = timeFormatter.format(time);
        int i = Arrays.binarySearch(BASE_TIME_THRESHOLDS, currentTime);

        LocalDateTime targetDateTime;
        int targetTimeIndex;

        if (i >= 0) {
            // 현재 시각이 주어진 배열값에 정확히 존재함
            // 해당 값으로 발표 시각 설정
            targetDateTime = time;
            targetTimeIndex = i;
        } else {
            // 현재 시각이 주어진 배열값에 없는 시각임

            int insertionPoint = -(i + 1);
            int index = insertionPoint - 1;

            if (index >= 0) {
                // 현재 시각이 0215 이후일 때
                // 당일 가장 근접한 발표 시각으로 설정
                targetDateTime = time;
                targetTimeIndex = index;
            } else {
                // 현재 시각이 0215 이전일 때
                // **하루 전날** 마지막 발표 시각으로 설정
                targetDateTime = time.minusDays(1);
                targetTimeIndex = BASE_TIMES.length - 1;
            }
        }

        String targetDate = baseDateFormatter.format(targetDateTime);
        String targetTime = BASE_TIMES[targetTimeIndex];

        return new ShortTermBaseDateTimeInfo(targetDate, targetTime);
    }
}
