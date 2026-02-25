package com.entreprise.sav.dto;

import com.entreprise.sav.enumeration.InteractionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record UpdateInteractionDto(
    InteractionType type,

    @NotBlank(message = "Le résumé ne peut pas être vide")
    String summary,

    @PastOrPresent(message = "La date de l'interaction ne peut pas être dans le futur")
    LocalDateTime occurred_at,

    @Positive(message = "La durée doit être positive")
    Integer duration,

    @NotBlank(message = "Le nom du commercial ne peut pas être vide")
    String commercial
) {}
