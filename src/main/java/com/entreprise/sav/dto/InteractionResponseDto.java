package com.entreprise.sav.dto;

import com.entreprise.sav.enumeration.InteractionType;

import java.time.LocalDateTime;

public record InteractionResponseDto(
    Long id,
    Long client_id,
    String client_name,
    InteractionType type,
    String summary,
    LocalDateTime occurred_at,
    Integer duration,
    String commercial,
    LocalDateTime created_at
) {}
