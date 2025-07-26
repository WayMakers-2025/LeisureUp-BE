package org.leisureup.global.response.external.weather;

import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.codec.*;
import java.util.concurrent.*;
import lombok.*;
import org.leisureup.global.response.external.*;
import org.springframework.context.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WeatherApiConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DefaultFeignErrorDecoder();
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objMapper) {
        return new WeatherApiDecoder(objMapper);
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 3);
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(
                3, TimeUnit.SECONDS,
                3, TimeUnit.SECONDS,
                false
        );
    }
}
