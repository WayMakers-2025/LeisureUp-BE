package org.leisureup.global.config;

import jakarta.annotation.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;

@Slf4j
@Configuration
public class DefaultTimeZoneConfig {

    private static final String DEFAULT_TIMEZONE = "Asia/Seoul";

    @PostConstruct
    public void setDefaultTimeZone() {
        log.info(
                "Setting default time zone to: [{}]",
                DEFAULT_TIMEZONE
        );

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

        log.info(
                "Time zone has been set to : [{}]",
                TimeZone.getDefault().getID()
        );
    }
}
