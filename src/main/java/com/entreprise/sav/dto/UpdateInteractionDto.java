package com.entreprise.sav.dto;

import com.entreprise.sav.enumeration.InteractionType;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateInteractionDto(
    InteractionType type,

    String summary,

    @PastOrPresent(message = "La date de l'interaction ne peut pas être dans le futur")
    LocalDateTime occurred_at,

    @Positive(message = "La durée doit être positive")
    Integer duration,

    String commercial
) {}
