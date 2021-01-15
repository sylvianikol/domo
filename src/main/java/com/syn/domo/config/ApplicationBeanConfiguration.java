package com.syn.domo.config;

import com.syn.domo.utils.UrlCheckerUtil;
import com.syn.domo.utils.UrlCheckerUtilImpl;
import com.syn.domo.utils.ValidationUtil;
import com.syn.domo.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public UrlCheckerUtil urlCheckerUtil() {
        return new UrlCheckerUtilImpl();
    }


}
