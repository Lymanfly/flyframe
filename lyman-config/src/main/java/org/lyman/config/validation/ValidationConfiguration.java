package org.lyman.config.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ValidatorFactory;

@Configuration
public class ValidationConfiguration {

    @Bean
    public ValidatorFactory validatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @Autowired
    public MethodValidationPostProcessor methodValidationPostProcessor(ValidatorFactory validatorFactory) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidatorFactory(validatorFactory);
        return processor;
    }

}
