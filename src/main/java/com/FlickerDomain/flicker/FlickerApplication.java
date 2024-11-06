package com.FlickerDomain.flicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.FlickerDomain.flicker")
@EnableJpaRepositories(basePackages = "com.FlickerDomain.flicker")
@EntityScan(basePackages = "com.FlickerDomain.flicker")
public class FlickerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlickerApplication.class, args);
    }
}
