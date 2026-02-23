package com.entreprise.sav.services;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.InteractionStatsDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.entities.Interaction;
import com.entreprise.sav.enumeration.InteractionType;
import com.entreprise.sav.exceptions.NotFoundException;
import com.entreprise.sav.mappers.ClientMapper;
import com.entreprise.sav.mappers.InteractionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientService {

    public List<Client> listClients() {
        return Client.listAll();
    }

    public Client getClient(Long id) {
        Client client = Client.findById(id);
        if (client == null) {
            throw new NotFoundException("Client introuvable");
        }
        return client;
    }

    @Transactional
    public Client createClient(CreateClientDto dto) {
        Client client = ClientMapper.toEntity(dto);
        client.persist();
        return client;
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = getClient(id);
        client.softDelete();
        client.persist();
    }

    public InteractionStatsDto getClientStats(Long id) {
        Client client = getClient(id);

        List<Interaction> interactions = Interaction.list("client", client);
        
        long totalInteractions = interactions.size();
        long totalDuration = interactions.stream()
                .filter(i -> i.duration != null)
                .mapToInt(i -> i.duration)
                .sum();
                
        Map<InteractionType, Long> breakdown = interactions.stream()
                .collect(Collectors.groupingBy(i -> i.type, Collectors.counting()));
                
        // Initialize all types with 0 if missing
        for (InteractionType t : InteractionType.values()) {
            breakdown.putIfAbsent(t, 0L);
        }

        return new InteractionStatsDto(
                id,
                totalInteractions,
                totalDuration,
                breakdown
        );
    }

    public List<InteractionResponseDto> getClientInteractions(Long id) {
        Client client = getClient(id);
        List<Interaction> interactions = Interaction.list("client", client);
        return InteractionMapper.toResponseDtoList(interactions);
    }
}
