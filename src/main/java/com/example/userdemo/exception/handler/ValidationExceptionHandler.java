package com.example.userdemo.exception.handler;

import com.example.userdemo.dto.ResponseExceptionDto;
import com.example.userdemo.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseExceptionDto> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(new ResponseExceptionDto(BAD_REQUEST.value(),
                exception.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ResponseExceptionDto(BAD_REQUEST.value(),
                errors.toString()), BAD_REQUEST);
    }
}
