package com.example.backend.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Saldo insuficiente para transferência");
    }
}
