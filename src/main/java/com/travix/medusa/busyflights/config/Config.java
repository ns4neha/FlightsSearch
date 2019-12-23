package com.travix.medusa.busyflights.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.handlers.RestTemplateResponseErrorHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }
}
