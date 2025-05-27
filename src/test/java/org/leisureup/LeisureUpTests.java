package org.leisureup;

import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.modulith.core.*;

@Slf4j
@SpringBootTest
class LeisureUpTests {

    @Test
    void contextLoads() {
        ApplicationModules applicationModules = ApplicationModules.of(LeisureUp.class);

        applicationModules.forEach(m -> log.info("Module: {}", m));
        applicationModules.verify();
    }

}
