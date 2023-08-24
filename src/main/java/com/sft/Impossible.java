package com.sft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@PropertySource(value = {
        "classpath:/persistence.properties"
})
public class Impossible {
    public static void main(String[] args) {
        SpringApplication.run(Impossible.class, args);
    }
}