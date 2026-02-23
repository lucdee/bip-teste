package com.example.backend.controller;

import com.example.backend.exception.BeneficioNotFoundException;
import com.example.backend.exception.InactiveBeneficioException;
import com.example.backend.exception.InsufficientBalanceException;
import com.example.backend.exception.InvalidTransferException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BeneficioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(BeneficioNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidTransfer(InvalidTransferException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler({InactiveBeneficioException.class, InsufficientBalanceException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleBusinessConflict(RuntimeException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return Map.of("message", "Erro de validação", "errors", errors);
    }
}
