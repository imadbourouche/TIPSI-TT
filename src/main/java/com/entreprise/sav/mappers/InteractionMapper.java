package com.entreprise.sav.mappers;

import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.entities.Interaction;

import java.util.List;

public class InteractionMapper {
    public static Interaction toEntity(CreateInteractionDto dto, Client client) {
        Interaction interaction = new Interaction();
        interaction.client = client;
        interaction.type = dto.type();
        interaction.summary = dto.summary();
        interaction.occurredAt = dto.occurred_at();
        interaction.duration = dto.duration();
        interaction.commercial = dto.commercial();
        return interaction;
    }

    public static void patchEntity(Interaction interaction, UpdateInteractionDto dto) {
        if (dto.type() != null) {
            interaction.type = dto.type();
        }
        
        if (dto.summary() != null && !dto.summary().isBlank()) {
            interaction.summary = dto.summary();
        }

        if (dto.commercial() != null && !dto.commercial().isBlank()) {
            interaction.commercial = dto.commercial();
        }

        if (dto.occurred_at() != null) {
            interaction.occurredAt = dto.occurred_at();
        }
        
        if (dto.duration() != null) {
            interaction.duration = dto.duration();
        }
    }

    public static InteractionResponseDto toResponseDto(Interaction interaction) {
        return new InteractionResponseDto(
                interaction.id,
                interaction.client.id,
                interaction.client.name,
                interaction.type,
                interaction.summary,
                interaction.occurredAt,
                interaction.duration,
                interaction.commercial,
                interaction.createdAt
        );
    }

    public static List<InteractionResponseDto> toResponseDtoList(List<Interaction> interactions) {
        return interactions.stream()
                .map(InteractionMapper::toResponseDto)
                .toList();
    }
}
