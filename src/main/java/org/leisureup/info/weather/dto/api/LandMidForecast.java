package org.leisureup.info.weather.dto.api;

public record LandMidForecast(
        String regId,
        int rnSt4Am, int rnSt4Pm,
        int rnSt5Am, int rnSt5Pm,
        int rnSt6Am, int rnSt6Pm,
        int rnSt7Am, int rnSt7Pm,
        int rnSt8, int rnSt9, int rnSt10,
        String wf4Am, String wf4Pm,
        String wf5Am, String wf5Pm,
        String wf6Am, String wf6Pm,
        String wf7Am, String wf7Pm,
        String wf8, String wf9, String wf10
) {

}
