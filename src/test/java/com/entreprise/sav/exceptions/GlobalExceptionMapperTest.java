package com.entreprise.sav.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalExceptionMapperTest {

    private final GlobalExceptionMapper mapper = new GlobalExceptionMapper();

    @Test
    @SuppressWarnings("unchecked")
    public void should_map_bad_request_exception_to_400() {
        BadRequestException exception = new BadRequestException("Bad request error");
        Response response = mapper.toResponse(exception);

        assertEquals(400, response.getStatus());
        Map<String, Object> entity = (Map<String, Object>) response.getEntity();
        assertEquals("Bad request error", entity.get("error"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_map_not_found_exception_to_404() {
        NotFoundException exception = new NotFoundException("Not found error");
        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());
        Map<String, Object> entity = (Map<String, Object>) response.getEntity();
        assertEquals("Not found error", entity.get("error"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_map_constraint_violation_exception_to_400() {
        ConstraintViolationException exception = new ConstraintViolationException(new HashSet<>());
        Response response = mapper.toResponse(exception);

        assertEquals(400, response.getStatus());
        Map<String, Object> entity = (Map<String, Object>) response.getEntity();
        assertEquals("Erreur de validation", entity.get("error"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_map_generic_exception_to_500() {
        Exception exception = new Exception("Generic error");
        Response response = mapper.toResponse(exception);

        assertEquals(500, response.getStatus());
        Map<String, Object> entity = (Map<String, Object>) response.getEntity();
        assertTrue(entity.get("error").toString().contains("Generic error"));
    }
}
