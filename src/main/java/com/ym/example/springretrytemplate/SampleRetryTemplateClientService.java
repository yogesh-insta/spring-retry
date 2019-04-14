package com.ym.example.springretrytemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleRetryTemplateClientService {

    @Autowired
    private SampleRetryService sampleRetryService;

    public void callRetryService() throws Exception {
        sampleRetryService.test("test123");
    }
}
