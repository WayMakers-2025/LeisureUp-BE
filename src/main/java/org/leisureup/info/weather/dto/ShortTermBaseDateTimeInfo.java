package org.leisureup.info.weather.dto;

import java.io.*;

public record ShortTermBaseDateTimeInfo(
        String baseDate,
        String baseTime
) implements Serializable {

}
