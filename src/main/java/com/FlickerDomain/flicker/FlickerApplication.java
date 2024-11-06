package com.FlickerDomain.flicker;

import com.FlickerDomain.flicker.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;  // Updated to Jakarta

@SpringBootApplication
@EntityScan(basePackages = "com.FlickerDomain.flicker.model")
public class FlickerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlickerApplication.class, args);
    }

    @PostConstruct
    public void checkEntityScan() {
        System.out.println("User entity is: " + User.class);
    }
}
