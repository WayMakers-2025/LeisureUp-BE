package org.leisureup.global.config;

import java.util.concurrent.*;
import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.*;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncExecutorConfig {

    private final RequestAndTraceIdDecorator requestAndTraceIdDecorator;

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.setTaskDecorator(requestAndTraceIdDecorator);
        executor.initialize();
        return executor;
    }
}
