package com.entreprise.sav.services;

import com.entreprise.sav.dto.CreateInteractionDto;
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

    public List<InteractionResponseDto> listInteractions(Long clientId, String type, String commercial, String from, String to) {
        StringJoiner query = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (clientId != null) {
            query.add("client.id = :clientId");
            params.put("clientId", clientId);
        }

        if (type != null && !type.trim().isEmpty()) {
            try {
                InteractionType interactionType = InteractionType.valueOf(type.toUpperCase());
                query.add("type = :type");
                params.put("type", interactionType);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Type invalide. Valeurs acceptées: CALL, EMAIL, MEETING, MESSAGE");
            }
        }

        if (commercial != null && !commercial.trim().isEmpty()) {
            query.add("commercial = :commercial");
            params.put("commercial", commercial);
        }

        if (from != null && !from.trim().isEmpty()) {
            LocalDate fromDate = LocalDate.parse(from);
            query.add("occurredAt >= :from");
            params.put("from", fromDate.atStartOfDay());
        }

        if (to != null && !to.trim().isEmpty()) {
            LocalDate toDate = LocalDate.parse(to);
            query.add("occurredAt <= :to");
            params.put("to", toDate.atTime(23, 59, 59));
        }

        List<Interaction> interactions;
        if (query.length() > 0) {
            interactions = Interaction.list(query.toString(), params);
        } else {
            interactions = Interaction.listAll();
        }

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
