package com.entreprise.sav.exceptions;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message);
    }
}
