package org.leisureup.info.recommend.internal.service;

import org.springframework.context.annotation.*;

@Configuration
public class ScoringConfig {

    @Bean
    public Scoring scoring() {
        return new DefaultScoring();
    }
}
