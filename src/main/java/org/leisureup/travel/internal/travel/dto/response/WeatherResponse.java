package org.leisureup.travel.internal.travel.dto.response;

import lombok.*;
import org.leisureup.travel.internal.travel.dto.TemperatureApiResponse;
import org.leisureup.travel.internal.travel.dto.WeatherApiResponse;
import org.leisureup.travel.internal.travel.service.Temperature;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private LocalDate date;
    private String weather;
    private String temperature;
    private int precipitation;

    public static List<WeatherResponse> fromApi(LocalDate now,
                                                TemperatureApiResponse temperature,
                                                WeatherApiResponse weather) {
        List<WeatherResponse> weatherResponses = new ArrayList<>();
        
        if (temperature == null || weather == null || 
            temperature.getBody() == null || weather.getBody() == null ||
            temperature.getBody().getItems() == null || weather.getBody().getItems() == null ||
            temperature.getBody().getItems().getItem() == null || weather.getBody().getItems().getItem() == null ||
            temperature.getBody().getItems().getItem().isEmpty() || weather.getBody().getItems().getItem().isEmpty()) {
            return weatherResponses;
        }
        
        TemperatureApiResponse.Item tempItem = temperature.getBody().getItems().getItem().get(0);
        WeatherApiResponse.Item weatherItem = weather.getBody().getItems().getItem().get(0);
        
        // 4일 후부터 10일 후까지의 날씨 정보 생성
        for (int i = 4; i <= 10; i++) {
            LocalDate targetDate = now.plusDays(i);
            
            // 온도 정보 가져오기
            String tempInfo = getTemperatureInfo(tempItem, i);
            
            // 날씨 정보 가져오기
            String weatherInfo = getWeatherInfo(weatherItem, i);
            
            // 강수 확률 정보 가져오기
            int precipitation = getPrecipitationInfo(weatherItem, i);
            
            WeatherResponse response = WeatherResponse.builder()
                    .date(targetDate)
                    .weather(weatherInfo)
                    .temperature(tempInfo)
                    .precipitation(precipitation)
                    .build();
            
            weatherResponses.add(response);
        }
        
        return weatherResponses;
    }
    
    private static String getTemperatureInfo(TemperatureApiResponse.Item tempItem, int day) {
        Integer minTemp = null;
        Integer maxTemp = null;
        
        switch (day) {
            case 4:
                minTemp = tempItem.getTaMin4();
                maxTemp = tempItem.getTaMax4();
                break;
            case 5:
                minTemp = tempItem.getTaMin5();
                maxTemp = tempItem.getTaMax5();
                break;
            case 6:
                minTemp = tempItem.getTaMin6();
                maxTemp = tempItem.getTaMax6();
                break;
            case 7:
                minTemp = tempItem.getTaMin7();
                maxTemp = tempItem.getTaMax7();
                break;
            case 8:
                minTemp = tempItem.getTaMin8();
                maxTemp = tempItem.getTaMax8();
                break;
            case 9:
                minTemp = tempItem.getTaMin9();
                maxTemp = tempItem.getTaMax9();
                break;
            case 10:
                minTemp = tempItem.getTaMin10();
                maxTemp = tempItem.getTaMax10();
                break;
        }
        
        if (minTemp != null && maxTemp != null) {
            return minTemp + "°C / " + maxTemp + "°C";
        } else if (minTemp != null) {
            return minTemp + "°C";
        } else if (maxTemp != null) {
            return maxTemp + "°C";
        } else {
            return "정보 없음";
        }
    }
    
    private static String getWeatherInfo(WeatherApiResponse.Item weatherItem, int day) {
        String weatherInfo = "";
        
        switch (day) {
            case 4:
                String wf4Am = weatherItem.getWf4Am();
                String wf4Pm = weatherItem.getWf4Pm();
                if (wf4Am != null && wf4Pm != null) {
                    weatherInfo = wf4Am + " / " + wf4Pm;
                } else if (wf4Am != null) {
                    weatherInfo = wf4Am;
                } else if (wf4Pm != null) {
                    weatherInfo = wf4Pm;
                }
                break;
            case 5:
                String wf5Am = weatherItem.getWf5Am();
                String wf5Pm = weatherItem.getWf5Pm();
                if (wf5Am != null && wf5Pm != null) {
                    weatherInfo = wf5Am + " / " + wf5Pm;
                } else if (wf5Am != null) {
                    weatherInfo = wf5Am;
                } else if (wf5Pm != null) {
                    weatherInfo = wf5Pm;
                }
                break;
            case 6:
                String wf6Am = weatherItem.getWf6Am();
                String wf6Pm = weatherItem.getWf6Pm();
                if (wf6Am != null && wf6Pm != null) {
                    weatherInfo = wf6Am + " / " + wf6Pm;
                } else if (wf6Am != null) {
                    weatherInfo = wf6Am;
                } else if (wf6Pm != null) {
                    weatherInfo = wf6Pm;
                }
                break;
            case 7:
                String wf7Am = weatherItem.getWf7Am();
                String wf7Pm = weatherItem.getWf7Pm();
                if (wf7Am != null && wf7Pm != null) {
                    weatherInfo = wf7Am + " / " + wf7Pm;
                } else if (wf7Am != null) {
                    weatherInfo = wf7Am;
                } else if (wf7Pm != null) {
                    weatherInfo = wf7Pm;
                }
                break;
            case 8:
                weatherInfo = weatherItem.getWf8();
                break;
            case 9:
                weatherInfo = weatherItem.getWf9();
                break;
            case 10:
                weatherInfo = weatherItem.getWf10();
                break;
        }
        
        return weatherInfo != null ? weatherInfo : "정보 없음";
    }
    
    private static int getPrecipitationInfo(WeatherApiResponse.Item weatherItem, int day) {
        String precipitationStr = "";
        
        switch (day) {
            case 4:
                String rnSt4Am = weatherItem.getRnSt4Am();
                String rnSt4Pm = weatherItem.getRnSt4Pm();
                if (rnSt4Am != null && rnSt4Pm != null) {
                    precipitationStr = rnSt4Am + "/" + rnSt4Pm;
                } else if (rnSt4Am != null) {
                    precipitationStr = rnSt4Am;
                } else if (rnSt4Pm != null) {
                    precipitationStr = rnSt4Pm;
                }
                break;
            case 5:
                String rnSt5Am = weatherItem.getRnSt5Am();
                String rnSt5Pm = weatherItem.getRnSt5Pm();
                if (rnSt5Am != null && rnSt5Pm != null) {
                    precipitationStr = rnSt5Am + "/" + rnSt5Pm;
                } else if (rnSt5Am != null) {
                    precipitationStr = rnSt5Am;
                } else if (rnSt5Pm != null) {
                    precipitationStr = rnSt5Pm;
                }
                break;
            case 6:
                String rnSt6Am = weatherItem.getRnSt6Am();
                String rnSt6Pm = weatherItem.getRnSt6Pm();
                if (rnSt6Am != null && rnSt6Pm != null) {
                    precipitationStr = rnSt6Am + "/" + rnSt6Pm;
                } else if (rnSt6Am != null) {
                    precipitationStr = rnSt6Am;
                } else if (rnSt6Pm != null) {
                    precipitationStr = rnSt6Pm;
                }
                break;
            case 7:
                String rnSt7Am = weatherItem.getRnSt7Am();
                String rnSt7Pm = weatherItem.getRnSt7Pm();
                if (rnSt7Am != null && rnSt7Pm != null) {
                    precipitationStr = rnSt7Am + "/" + rnSt7Pm;
                } else if (rnSt7Am != null) {
                    precipitationStr = rnSt7Am;
                } else if (rnSt7Pm != null) {
                    precipitationStr = rnSt7Pm;
                }
                break;
            case 8:
                precipitationStr = weatherItem.getRnSt8();
                break;
            case 9:
                precipitationStr = weatherItem.getRnSt9();
                break;
            case 10:
                precipitationStr = weatherItem.getRnSt10();
                break;
        }
        
        // 강수 확률을 정수로 변환 (예: "30" -> 30)
        if (precipitationStr != null && !precipitationStr.isEmpty()) {
            try {
                // "/"로 구분된 경우 첫 번째 값 사용
                String[] parts = precipitationStr.split("/");
                return Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        return 0;
    }
}
