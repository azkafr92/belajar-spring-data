package com.azkafadhli.belajarspringdata.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanRegistration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
