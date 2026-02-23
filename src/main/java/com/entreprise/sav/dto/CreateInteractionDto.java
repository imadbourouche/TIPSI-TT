package com.entreprise.sav.dto;

import com.entreprise.sav.enumeration.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateInteractionDto(    
    @NotNull(message = "Le type est obligatoire")
    InteractionType type,
    
    @NotBlank(message = "Le résumé est obligatoire")
    String summary,
    
    @NotNull(message = "La date de l'interaction est obligatoire")
    @PastOrPresent(message = "La date de l'interaction ne peut pas être dans le futur")
    LocalDateTime occurred_at,
    
    @Positive(message = "La durée doit être positive")
    Integer duration,
    
    @NotBlank(message = "Le commercial responsable est obligatoire")
    String commercial,

    @NotNull(message = "Le client_id est obligatoire")
    Long client_id
) {}
