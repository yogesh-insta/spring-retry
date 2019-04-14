package com.ym.example.springretry;

import com.ym.example.springretrytemplate.SampleRetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class SpringRetryApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRetryService.class);

    @Configuration
    public class AppConfig {
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringRetryApplication.class, args);
    }

}
