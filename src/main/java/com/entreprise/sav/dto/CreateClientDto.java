package com.entreprise.sav.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateClientDto(
    @NotBlank(message = "Le nom du client est obligatoire")
    String name,
    
    @NotBlank(message = "Le secteur d'activité est obligatoire")
    String sector
) {}
