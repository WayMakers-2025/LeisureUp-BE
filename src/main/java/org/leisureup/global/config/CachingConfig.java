package org.leisureup.global.config;

import java.time.*;
import org.springframework.cache.*;
import org.springframework.cache.annotation.*;
import org.springframework.cache.support.*;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.*;

@EnableCaching
@Configuration
public class CachingConfig {

    private static final long DEFAULT_CACHE_TTL_MIN = 30;

    @Bean
    @Profile("!test")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(DEFAULT_CACHE_TTL_MIN))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    @Profile("test")
    public CacheManager testCacheManager() {
        return new NoOpCacheManager();
    }
}
