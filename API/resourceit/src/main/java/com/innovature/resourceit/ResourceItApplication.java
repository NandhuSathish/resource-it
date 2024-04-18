package com.innovature.resourceit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories
@EntityScan("com.innovature.resourceit.entity")
@EnableAsync
public class ResourceItApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceItApplication.class, args);
    }

}
