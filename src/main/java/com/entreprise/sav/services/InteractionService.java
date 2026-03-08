package com.entreprise.sav.services;

import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionFilter;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.entities.Interaction;
import com.entreprise.sav.enumeration.InteractionType;
import com.entreprise.sav.exceptions.BadRequestException;
import com.entreprise.sav.exceptions.NotFoundException;
import com.entreprise.sav.mappers.InteractionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@ApplicationScoped
public class InteractionService {

    @Inject
    ClientService clientService;

    public List<InteractionResponseDto> listInteractions(InteractionFilter filter) {
        InteractionType type = null;
        if (filter.type() != null && !filter.type().isBlank()) {
            try {
                type = InteractionType.valueOf(filter.type().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid type: " + filter.type());
            }
        }
        LocalDateTime fromDate = (filter.from() != null) ? LocalDate.parse(filter.from()).atStartOfDay() : null;
        LocalDateTime toDate = (filter.to() != null) ? LocalDate.parse(filter.to()).atTime(23, 59, 59) : null;

        List<Interaction> interactions = Interaction.findFiltered(filter.client_id(), type, filter.commercial(), fromDate, toDate).list();

        return InteractionMapper.toResponseDtoList(interactions);
    }
 
    @Transactional
    public InteractionResponseDto createInteraction(CreateInteractionDto dto) {
        Client client;
        try {
            client = clientService.getClient(dto.client_id());
        } catch (NotFoundException e) {
            throw new BadRequestException("Client introuvable");
        }

        if (client.deletedAt != null) {
            throw new BadRequestException("Impossible de créer une interaction pour un client supprimé");
        }

        if (dto.occurred_at().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("La date de l'interaction ne peut pas être dans le futur");
        }

        Interaction interaction = InteractionMapper.toEntity(dto, client);
        interaction.persist();
        return InteractionMapper.toResponseDto(interaction);
    }

    public InteractionResponseDto getInteractionResponseDto(Long id) {
        return InteractionMapper.toResponseDto(getInteraction(id));
    }

    private Interaction getInteraction(Long id) {
        Interaction interaction = Interaction.findById(id);
        if (interaction == null) {
            throw new NotFoundException("Interaction introuvable");
        }
        return interaction;
    }

    @Transactional
    public InteractionResponseDto updateInteraction(Long id, UpdateInteractionDto dto) {
        Interaction interaction = getInteraction(id);

        if (dto.occurred_at() != null && dto.occurred_at().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("La date de l'interaction ne peut pas être dans le futur");
        }

        InteractionMapper.patchEntity(interaction, dto);
        interaction.persist();
        return InteractionMapper.toResponseDto(interaction);
    }

    @Transactional
    public void deleteInteraction(Long id) {
        Interaction interaction = getInteraction(id);
        interaction.delete();
    }
}
