package org.sterl.training.springquartz.quartz;

import java.util.Map;

import org.quartz.ObjectAlreadyExistsException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final ErrorAttributeOptions OPTIONS = ErrorAttributeOptions.of(Include.values());

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleObjectAlreadyExistsException(
            ServletWebRequest r, ObjectAlreadyExistsException e) {
        return of(HttpStatus.CONFLICT, r, e);
    }

    private ResponseEntity<Map<String, Object>> of (HttpStatus status, ServletWebRequest r, Exception e) {
        Map<String, Object> result = new DefaultErrorAttributes().getErrorAttributes(r, OPTIONS);
        result.put("error", status.getReasonPhrase());
        result.put("status", status.value());
        result.remove("trace");
        result.put("path", r.getRequest().getRequestURI());
        return ResponseEntity.status(status).body(result);
    }
}
