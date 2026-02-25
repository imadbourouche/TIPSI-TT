package com.entreprise.sav.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExceptionTest {

    @Test
    public void should_create_bad_request_exception() {
        BadRequestException ex = new BadRequestException("bad request message");
        assertEquals("bad request message", ex.getMessage());
    }

    @Test
    public void should_create_not_found_exception() {
        NotFoundException ex = new NotFoundException("not found message");
        assertEquals("not found message", ex.getMessage());
    }
    
    @Test
    public void should_create_business_exception() {
        BusinessException ex = new BusinessException("business message") {};
        assertEquals("business message", ex.getMessage());
    }
}
