package org.leisureup;

import org.leisureup.global.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.modulith.*;
import org.springframework.retry.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.bind.annotation.*;

@Modulithic
@EnableRetry
@EnableScheduling
@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableFeignClients(basePackages = "org.leisureup")
public class LeisureUp {

    public static void main(String[] args) {
        SpringApplication.run(LeisureUp.class, args);
    }

    @RestController
    protected static class HealthController {

        private final String appType;

        public HealthController(
                @Value("${app-type}") String appType
        ) {
            this.appType = appType;
        }

        @GetMapping("/health")
        public ApiResponse<Status> health() {
            return ApiResponse.success(
                    200, new Status(this.appType)
            );
        }
    }

    protected record Status(
            String type
    ) {

    }
}
