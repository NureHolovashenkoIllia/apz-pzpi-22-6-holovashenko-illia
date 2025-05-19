package ua.nure.holovashenko.flameguard_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlameGuardApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlameGuardApiApplication.class, args);
    }

}