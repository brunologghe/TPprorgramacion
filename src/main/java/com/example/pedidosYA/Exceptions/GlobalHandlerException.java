package com.example.pedidosYA.Exceptions;

import com.example.pedidosYA.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice

public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> HandlerArgumentException(MethodArgumentNotValidException ex){

        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors().stream().collect(Collectors.toMap(
                        FieldError::getField, FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> HandlerBusinessException(BusinessException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }



}
