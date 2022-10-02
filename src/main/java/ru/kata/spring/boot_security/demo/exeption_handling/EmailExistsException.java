package ru.kata.spring.boot_security.demo.exeption_handling;

public class EmailExistsException extends RuntimeException {
    private final String message = "email exists";

    public String getMessage(){
        return message;
    }
}
