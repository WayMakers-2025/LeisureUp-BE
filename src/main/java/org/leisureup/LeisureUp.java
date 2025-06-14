package org.leisureup;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.modulith.*;

@Modulithic
@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableFeignClients(basePackages = "org.leisureup")
public class LeisureUp {

    public static void main(String[] args) {
        SpringApplication.run(LeisureUp.class, args);
    }

}
