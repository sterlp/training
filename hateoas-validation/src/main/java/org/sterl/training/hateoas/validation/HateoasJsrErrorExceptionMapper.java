package org.sterl.training.hateoas.validation;

import java.beans.Introspector;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.data.rest.webmvc.support.ConstraintViolationExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.sterl.training.hateoas.validation.HateoasRepositoryValidationListenerConfig.RepositoryListenerValidationException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;

/**
 * @see RepositoryRestExceptionHandler
 * @see DefaultErrorAttributes
 * @see ConstraintViolationExceptionMessage
 */
@ControllerAdvice
public class HateoasJsrErrorExceptionMapper {

    private SpringValidatorBridge validatorBridge;
    
    @Autowired
    public HateoasJsrErrorExceptionMapper(Validator validator) {
        validatorBridge = new SpringValidatorBridge(validator);
    }
    
    // Solution 2, extract ConstraintViolationException from the TransactionSystemException
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleConstraintViolation(TransactionSystemException e, ServletWebRequest request) {
        ResponseEntity<Object> result;
        if (e.getRootCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException)e.getRootCause();
            
            BeanPropertyBindingResult errors = validatorBridge.processConstraintViolations(cve);
            result = ResponseEntity.badRequest().body(ValidationError.builder()
                    .message(cve.getMessage())
                    .path(request.getRequest().getRequestURI())
                    .errors(errors.getAllErrors())
                    .build());
        } else {
            result = null; // not handled here
        }
        return result;
    }
    
    // Solution 1, use a custom exception and validated on our own
    @ExceptionHandler(RepositoryListenerValidationException.class)
    public ResponseEntity<Object> handleConstraintViolation(RepositoryListenerValidationException e, ServletWebRequest request) {
        return ResponseEntity.badRequest().body(ValidationError.builder()
                .message(e.getResult().getAllErrors().get(0).getDefaultMessage())
                .path(request.getRequest().getRequestURI())
                .errors(e.getResult().getAllErrors())
                .build());
        
    }
    
    // Simple model class which represents our DefaultErrorAttributes.
    // The alternative would be to use DefaultErrorAttributes and set the
    // error code etc. in the request
    @Builder @Getter
    @JsonPropertyOrder({"timestamp", "status", "error", "errors", "message", "path"})
    @JsonAutoDetect(isGetterVisibility = Visibility.PUBLIC_ONLY, fieldVisibility = Visibility.NONE)
    static class ValidationError {
        final Instant timestamp = Instant.now();
        final String message;
        final String path;
        final List<ObjectError> errors;
        public int getStatus() {
            return HttpStatus.BAD_REQUEST.value();
        }
        public String getError() {
            return HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
    }
    
    // simple bridge to get access to the processConstraintViolations method
    static class SpringValidatorBridge extends SpringValidatorAdapter {
        public SpringValidatorBridge(Validator validator) {
            super(validator);
        }
        public BeanPropertyBindingResult processConstraintViolations(ConstraintViolationException cve) {
            // get the bean which was validated
            final Object bean = cve.getConstraintViolations().iterator().next().getRootBean();
    
            final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(bean, 
                    Introspector.decapitalize(bean.getClass().getSimpleName()));
            // execute the spring code to map ConstraintViolations to ObjectError
            super.processConstraintViolations(map(cve.getConstraintViolations()), 
                    errors);
            return errors;
        }
    }
    // needed as we have to map between <?> and <Object>
    static Set<ConstraintViolation<Object>> map(Set<ConstraintViolation<?>> input) {
        return input.stream().map(v -> (ConstraintViolation<Object>)v).collect(Collectors.toSet());
    }
    
    /* Simpler class representing the fields of the Spring FieldError
    @Autowired private MessageSource messageSource;
    @Getter @ToString
    static class SimpleFieldError {
        String defaultMessage;
        String objectName;
        String field;
        Object rejectedValue;
        String code;
        
        public static SimpleFieldError from(ConstraintViolation<?> violation, MessageSource msgSrc, Locale locale) {
            SimpleFieldError result = new SimpleFieldError();
            result.defaultMessage = msgSrc.getMessage(violation.getMessageTemplate(),
                    new Object[] { violation.getLeafBean().getClass().getSimpleName(), violation.getPropertyPath().toString(),
                            violation.getInvalidValue() }, violation.getMessage(), locale);
            result.objectName = Introspector.decapitalize(violation.getRootBean().getClass().getSimpleName());
            result.field = String.valueOf(violation.getPropertyPath());
            result.rejectedValue = violation.getInvalidValue();
            result.code = violation.getMessageTemplate();
            return result;
        }
    }*/
    
}
