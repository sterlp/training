package org.sterl.training.hateoas.validation;

import java.beans.Introspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Solution 1 event listener
// @Service currently disabled
public class HateoasRepositoryValidationListenerConfig extends AbstractRepositoryEventListener<Object> {

    @Autowired private SpringValidatorAdapter validator;

    @Override
    protected void onBeforeCreate(Object entity) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, 
                Introspector.decapitalize(entity.getClass().getSimpleName()));
        validator.validate(entity, errors);
        // throw a custom exception containing the spring validation result
        // which we will use later in our controller exception mapper
        if (errors.hasErrors()) {
            throw new RepositoryListenerValidationException(errors);
        }
    }
    
    @AllArgsConstructor
    public static class RepositoryListenerValidationException extends RuntimeException {
        @Getter
        private final BeanPropertyBindingResult result;
    }
}
