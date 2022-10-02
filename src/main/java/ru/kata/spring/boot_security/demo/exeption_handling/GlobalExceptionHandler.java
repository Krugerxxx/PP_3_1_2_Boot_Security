package ru.kata.spring.boot_security.demo.exeption_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(NoSuchElementException e) {
        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setInfo(e.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleEmailException(EmailExistsException e) {
        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setInfo(e.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleEmailException(IncorrectDataException e) {
        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setInfo(e.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.NOT_ACCEPTABLE);
    }

}
