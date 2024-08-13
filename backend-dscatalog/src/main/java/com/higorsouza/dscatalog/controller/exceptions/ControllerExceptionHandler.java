package com.higorsouza.dscatalog.controller.exceptions;

import com.higorsouza.dscatalog.service.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> controllerNotFound(EntityNotFoundException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Controller not found");
        err.setMessage(e.getMessage());
        err.setCode("CAT-50");
        err.setPath(request.getRequestURI());
        log.error(err);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
}
