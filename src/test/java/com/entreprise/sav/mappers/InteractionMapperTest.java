package com.entreprise.sav.mappers;

import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.entities.Interaction;
import com.entreprise.sav.enumeration.InteractionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InteractionMapperTest {

    private Client mockClient;

    @BeforeEach
    public void setup() {
        mockClient = new Client();
        mockClient.id = 1L;
        mockClient.name = "Test Client";
    }

    @Test
    public void should_map_create_interaction_dto_to_entity() {
        LocalDateTime now = LocalDateTime.now();
        CreateInteractionDto dto = new CreateInteractionDto(InteractionType.CALL, "Test Summary", now, 30, "Alice", 1L);
        
        Interaction interaction = InteractionMapper.toEntity(dto, mockClient);

        assertNotNull(interaction);
        assertEquals(mockClient, interaction.client);
        assertEquals(InteractionType.CALL, interaction.type);
        assertEquals("Test Summary", interaction.summary);
        assertEquals(now, interaction.occurredAt);
        assertEquals(30, interaction.duration);
        assertEquals("Alice", interaction.commercial);
    }

    @Test
    public void should_patch_interaction_entity() {
        Interaction interaction = new Interaction();
        interaction.type = InteractionType.CALL;
        interaction.summary = "Old Summary";
        interaction.duration = 15;
        
        LocalDateTime newTime = LocalDateTime.now();
        UpdateInteractionDto dto = new UpdateInteractionDto(InteractionType.MEETING, "New Summary", newTime, 60, "Bob");
        
        InteractionMapper.patchEntity(interaction, dto);
        
        assertEquals(InteractionType.MEETING, interaction.type);
        assertEquals("New Summary", interaction.summary);
        assertEquals(newTime, interaction.occurredAt);
        assertEquals(60, interaction.duration);
        assertEquals("Bob", interaction.commercial);
    }

    @Test
    public void should_ignore_nulls_when_patching_entity() {
        Interaction interaction = new Interaction();
        interaction.type = InteractionType.CALL;
        interaction.summary = "Old Summary";
        
        UpdateInteractionDto dto = new UpdateInteractionDto(null, null, null, null, null);
        
        InteractionMapper.patchEntity(interaction, dto);
        
        assertEquals(InteractionType.CALL, interaction.type);
        assertEquals("Old Summary", interaction.summary);
    }

    @Test
    public void should_map_interaction_entity_to_response_dto() {
        Interaction interaction = new Interaction();
        interaction.id = 100L;
        interaction.client = mockClient;
        interaction.type = InteractionType.EMAIL;
        interaction.summary = "Email summary";
        interaction.occurredAt = LocalDateTime.now();
        interaction.duration = 5;
        interaction.commercial = "Charlie";
        interaction.createdAt = LocalDateTime.now();
        
        InteractionResponseDto dto = InteractionMapper.toResponseDto(interaction);
        
        assertNotNull(dto);
        assertEquals(100L, dto.id());
        assertEquals(1L, dto.client_id());
        assertEquals("Test Client", dto.client_name());
        assertEquals(InteractionType.EMAIL, dto.type());
        assertEquals("Email summary", dto.summary());
        assertEquals(interaction.occurredAt, dto.occurred_at());
        assertEquals(5, dto.duration());
        assertEquals("Charlie", dto.commercial());
        assertEquals(interaction.createdAt, dto.created_at());
    }

    @Test
    public void should_map_interaction_entities_to_response_dto_list() {
        Interaction i1 = new Interaction();
        i1.client = mockClient;
        
        Interaction i2 = new Interaction();
        i2.client = mockClient;
        
        List<InteractionResponseDto> list = InteractionMapper.toResponseDtoList(List.of(i1, i2));
        
        assertNotNull(list);
        assertEquals(2, list.size());
    }
}
