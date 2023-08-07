package com.example.taskmanager.controller;


import com.example.taskmanager.dto.ExceptionResponse;
import com.example.taskmanager.exception.InvalidPasswordException;
import com.example.taskmanager.exception.UserAlreadyExistException;
import com.example.taskmanager.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {



    @ExceptionHandler({InvalidPasswordException.class, UserAlreadyExistException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(RuntimeException runtimeException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(runtimeException.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(RuntimeException runtimeException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(runtimeException.getMessage()));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleNotValidData(MethodArgumentNotValidException exception){
        Map<String, String> errors = exception.getFieldErrors().stream()
                .filter(ex -> ex.getDefaultMessage()!= null)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.badRequest().body(errors);

    }


}
