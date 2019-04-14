package com.ym.example.springretrytemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringRetryTemplateApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringRetryTemplateApplicationTests.class);
    @Autowired
    private SampleRetryTemplateClientService client;

    @Test
    public void contextLoads() {
    }

    @Test
    public void sampleRetryService() {
        try {
            client.callRetryService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
