package com.entreprise.sav.dto;

import com.entreprise.sav.enumeration.InteractionType;

import java.util.Map;

public record InteractionStatsDto(
    Long client_id,
    long total_interactions,
    long total_duration_minutes,
    Map<InteractionType, Long> breakdown_by_type
) {}
