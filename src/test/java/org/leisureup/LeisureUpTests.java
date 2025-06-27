package org.leisureup;

import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.modulith.core.*;
import org.springframework.modulith.docs.*;

@Slf4j
@SpringBootTest
class LeisureUpTests {

    private final ApplicationModules modules
            = ApplicationModules.of(LeisureUp.class);

    @Test
    void contextLoads() {

    }

    @Test
    void verifyModules() {
        modules.forEach(m -> log.info("Module : {}", m));
        modules.verify();
    }

    @Test
    void writeDocument() {
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
