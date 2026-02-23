package com.entreprise.sav.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof BadRequestException bre) {
            return buildResponse(Response.Status.BAD_REQUEST, bre.getMessage());
        }
        
        if (exception instanceof NotFoundException nfe) {
            return buildResponse(Response.Status.NOT_FOUND, nfe.getMessage());
        }

        if (exception instanceof ConstraintViolationException cve) {
            // Get the first error message
            String message = cve.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Erreur de validation");
                
            return buildResponse(Response.Status.BAD_REQUEST, message);
        }
        
        // Handle generic errors
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error: " + exception.getMessage());
    }
    
    private Response buildResponse(Response.Status status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", status.getStatusCode());
        response.put("error", message);
        
        return Response.status(status).entity(response).build();
    }
}
