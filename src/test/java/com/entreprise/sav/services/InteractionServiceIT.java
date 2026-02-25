package com.entreprise.sav.services;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.enumeration.InteractionType;
import com.entreprise.sav.exceptions.BadRequestException;
import com.entreprise.sav.exceptions.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class InteractionServiceIT {

    @Inject
    InteractionService interactionService;

    @Inject
    ClientService clientService;

    private Client testClient;

    @BeforeEach
    public void setup() {
        CreateClientDto dto = new CreateClientDto("Interaction Corp", "Sales");
        testClient = clientService.createClient(dto);
    }

    @Test
    public void should_create_interaction() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.MEETING,
                "Project discussion",
                LocalDateTime.now().minusDays(1),
                60,
                "Salesman Bob",
                testClient.id
        );

        InteractionResponseDto response = interactionService.createInteraction(dto);

        assertNotNull(response.id());
        assertEquals(testClient.id, response.client_id());
        assertEquals(InteractionType.MEETING, response.type());
        assertEquals("Project discussion", response.summary());
        assertEquals(60, response.duration());
        assertEquals("Salesman Bob", response.commercial());
    }

    @Test
    public void should_throw_exception_when_creating_interaction_with_future_date() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.CALL,
                "Future call",
                LocalDateTime.now().plusDays(1),
                30,
                "Salesman Bob",
                testClient.id
        );

        assertThrows(BadRequestException.class, () -> {
            interactionService.createInteraction(dto);
        });
    }

    @Test
    public void should_throw_exception_when_creating_interaction_for_deleted_client() {
        clientService.deleteClient(testClient.id);

        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.CALL,
                "Call",
                LocalDateTime.now().minusDays(1),
                30,
                "Salesman Bob",
                testClient.id
        );

        assertThrows(BadRequestException.class, () -> {
            interactionService.createInteraction(dto);
        });
    }

    @Test
    public void should_update_interaction() {
        CreateInteractionDto createDto = new CreateInteractionDto(
                InteractionType.MESSAGE,
                "Initial message",
                LocalDateTime.now().minusDays(2),
                5,
                "Salesman Bob",
                testClient.id
        );
        InteractionResponseDto created = interactionService.createInteraction(createDto);

        UpdateInteractionDto updateDto = new UpdateInteractionDto(
                InteractionType.EMAIL,
                "Updated message",
                null,
                10,
                null
        );

        InteractionResponseDto updated = interactionService.updateInteraction(created.id(), updateDto);

        assertEquals(InteractionType.EMAIL, updated.type());
        assertEquals("Updated message", updated.summary());
        assertEquals(10, updated.duration());
        assertEquals("Salesman Bob", updated.commercial()); 
    }

    @Test
    public void should_delete_interaction() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.CALL,
                "To delete",
                LocalDateTime.now().minusDays(1),
                15,
                "Salesman Bob",
                testClient.id
        );
        InteractionResponseDto created = interactionService.createInteraction(dto);

        interactionService.deleteInteraction(created.id());

        assertThrows(NotFoundException.class, () -> {
            interactionService.getInteractionResponseDto(created.id());
        });
    }

    @Test
    public void should_list_interactions_with_filters() {
        CreateInteractionDto dto1 = new CreateInteractionDto(
                InteractionType.CALL, "Call 1", LocalDateTime.now().minusDays(5), 10, "Alice", testClient.id
        );
        interactionService.createInteraction(dto1);

        CreateInteractionDto dto2 = new CreateInteractionDto(
                InteractionType.MEETING, "Meeting 1", LocalDateTime.now().minusDays(2), 60, "Bob", testClient.id
        );
        interactionService.createInteraction(dto2);

        List<InteractionResponseDto> calls = interactionService.listInteractions(testClient.id, "CALL", null, null, null);
        assertFalse(calls.isEmpty());
        assertTrue(calls.stream().allMatch(i -> i.type() == InteractionType.CALL));

        List<InteractionResponseDto> bobs = interactionService.listInteractions(testClient.id, null, "Bob", null, null);
        assertFalse(bobs.isEmpty());
        assertTrue(bobs.stream().allMatch(i -> "Bob".equals(i.commercial())));

        String fromDate = LocalDateTime.now().minusDays(3).toLocalDate().toString();
        String toDate = LocalDateTime.now().toLocalDate().toString();
        List<InteractionResponseDto> recent = interactionService.listInteractions(testClient.id, null, null, fromDate, toDate);
        assertFalse(recent.isEmpty());
    }
}
