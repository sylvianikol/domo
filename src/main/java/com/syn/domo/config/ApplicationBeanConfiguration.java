package com.syn.domo.config;

import com.syn.domo.utils.ValidationUtil;
import com.syn.domo.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

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
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(System.getenv("MAIL_HOST"));
        mailSender.setUsername(System.getenv("MAIL_USER"));
        mailSender.setPassword(System.getenv("MAIL_PASSWORD"));
        mailSender.setPort(Integer.parseInt(System.getenv("MAIL_PORT")));

        Properties properties = new Properties();
        properties.setProperty("mail.from.email", System.getenv("MAIL_USER"));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
