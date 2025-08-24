package org.leisureup.info.weather.dto.response;

import java.io.*;
import java.util.*;
import org.leisureup.info.weather.dto.*;

public record WeatherWarningResponse(
        List<SingleWeatherWarning> parsedWarnings,
        RawWeatherWaringContent rawContent
) implements Serializable {

}
