package com.ym.example.springretrytemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@SpringBootApplication
public class SpringRetryTemplateApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRetryService.class);

    private int CREATE_ALERT_AFTER_X_ERRORS = 3;
    private long INCREASE_DELAY_AFTER_EVERY_X_ATTEMPTS = 3;
    private long INCREASE_DELAY_BY = 100l;
    private int MAX_ATTEMPT = 5;

    @Configuration
    public class AppConfig {
        @Bean
        public RetryTemplate retryTemplate() {
            RetryTemplate retryTemplate = new RetryTemplate();

            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(INCREASE_DELAY_BY);
            retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

            SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
            retryPolicy.setMaxAttempts(MAX_ATTEMPT);
            retryTemplate.setRetryPolicy(retryPolicy);

            retryTemplate.registerListener(new RetryListener() {
                @Override
                public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                    LOGGER.info("in Open");
                    return true;
                }

                @Override
                public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                    LOGGER.info("in Close");
                }

                @Override
                public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                    LOGGER.error("Exception occurred");

                    if (retryContext.getRetryCount() == CREATE_ALERT_AFTER_X_ERRORS) {
                        LOGGER.error("FAILED TO SEND NOTIFICATION TO SALESFORCE FOR {} TIMES", CREATE_ALERT_AFTER_X_ERRORS);
                    }

                    if (retryContext.getRetryCount() % INCREASE_DELAY_AFTER_EVERY_X_ATTEMPTS == 0) {
                        fixedBackOffPolicy.setBackOffPeriod(fixedBackOffPolicy.getBackOffPeriod() + INCREASE_DELAY_BY);
                        LOGGER.info("setting Backoff time to {}", fixedBackOffPolicy.getBackOffPeriod());
                    }
                }
            });

            return retryTemplate;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringRetryTemplateApplication.class, args);
    }

}
