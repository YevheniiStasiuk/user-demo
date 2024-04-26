package com.example.userdemo.exception.handler;

import com.example.userdemo.dto.ResponseExceptionDto;
import com.example.userdemo.exception.EntityAlreadyExistException;
import com.example.userdemo.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class EntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseExceptionDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(new ResponseExceptionDto(NOT_FOUND.value(),
                "Entity like this not found"), NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ResponseExceptionDto> handleEntityAlreadyExist(EntityAlreadyExistException exception) {
        return new ResponseEntity<>(new ResponseExceptionDto(CONFLICT.value(),
                "Entity like this already exist"), CONFLICT);
    }
}