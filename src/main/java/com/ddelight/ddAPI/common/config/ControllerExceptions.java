package com.ddelight.ddAPI.common.config;

import com.ddelight.ddAPI.common.exception.*;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptions {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String fileUploadSize;

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<String[]> handleDuplicateEmail(DuplicateEntityException ex) {
        return new ResponseEntity<>(new String[]{ex.getMessage()}, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List> handleInvalidFields(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error->error.getDefaultMessage()).collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<String[]> handleInvalidFile(InvalidFileTypeException ex) {
        return new ResponseEntity<>(new String[]{ex.getMessage()}, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<String[]> handleNoSuchEntity(NoSuchEntityException ex){
        return new ResponseEntity<>(new String[]{ex.getMessage()},HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String[]> response(MaxUploadSizeExceededException exception){
        return new ResponseEntity<>(new String[]{exception.getMessage()+": "+fileUploadSize}, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceededQuantityException.class)
    public ResponseEntity<String[]> handleRuntimeExceptions(ExceededQuantityException exception){
        return new ResponseEntity<>(new String[]{exception.getMessage()}, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String[]> handleUsernameNotFoundException(UsernameNotFoundException ex){
        return new ResponseEntity<>(new String[]{ex.getMessage()},HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoogleJsonResponseException.class)
    public ResponseEntity<String[]> handleGoogleJsonResponseException(GoogleJsonResponseException ex){
        return new ResponseEntity<>(new String[]{ex.getMessage()},HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IntentCreationException.class)
    public ResponseEntity<String[]> handlePaymentCreationException(IntentCreationException ex){
        return new ResponseEntity<>(new String[]{ex.getMessage()},HttpStatus.BAD_REQUEST);
    }

}
