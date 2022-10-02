package ru.kata.spring.boot_security.demo.exeption_handling;

public class IncorrectDataException extends RuntimeException {

    private final String message = "incorrect data";

    public String getMessage() {
        return message;
    }
}

