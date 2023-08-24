package com.sft.config;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    record JsonFieldError(String fieldId, String i18n, String errorType) {
    };

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse> handleMethodArgNotValidException(MethodArgumentNotValidException ex, Locale locale) {

        val errorFields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        new JsonFieldError(
                                fieldError.getField(),
                                requireNonNullElseGet(fieldError.getDefaultMessage(), () -> "").toLowerCase().replace(' ', '_'),
                                fieldError.getCode()))
                .toArray();

        // put errorMessage into ErrorResponse
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder(
                                ex,
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage()
                        ).detailMessageArguments(errorFields)
                        .build());
    }
}