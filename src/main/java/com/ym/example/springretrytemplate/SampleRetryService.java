package com.ym.example.springretrytemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class SampleRetryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRetryService.class);

    private static int COUNTER = 0;

    @Autowired
    private RetryTemplate retryTemplate;

    public void test(String input) throws Exception {
        retryTemplate.execute((RetryCallback<Void, Exception>) arg0 -> {
            SampleRetryService sampleRetryService2 = new SampleRetryService();
            sampleRetryService2.retryWhenException(input);
            return null;
        });
    }

    public String retryWhenException(String name) throws Exception {
        COUNTER++;
        LOGGER.info("COUNTER = " + COUNTER);

        if (COUNTER == 1) {
            throw new Exception("1");
        } else if (COUNTER == 2) {
            throw new Exception("2");
        } else if(COUNTER < 5) {
            throw new RuntimeException();
        }
        LOGGER.info("Successfully executed this time...");
        return "SUCCESS";
    }

    @Recover
    public String recover(Throwable t) {
        LOGGER.info("SampleRetryService.recover");
        return "Error Class :: " + t.getClass().getName();
    }
}
