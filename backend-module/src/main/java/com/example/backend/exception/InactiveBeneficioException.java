package com.example.backend.exception;

public class InactiveBeneficioException extends RuntimeException {

    public InactiveBeneficioException() {
        super("Só é permitido transferir entre benefícios ativos");
    }
}
