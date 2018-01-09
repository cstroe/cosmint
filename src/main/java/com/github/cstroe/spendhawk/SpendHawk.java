package com.github.cstroe.spendhawk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class SpendHawk {
    public static void main(String[] args) {
        SpringApplication.run(SpendHawk.class, args);
    }

    @Bean
    @Primary
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
