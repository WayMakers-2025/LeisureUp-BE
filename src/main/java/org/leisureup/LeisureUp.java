package org.leisureup;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.leisureup")
public class LeisureUp {

    public static void main(String[] args) {
        SpringApplication.run(LeisureUp.class, args);
    }

}
