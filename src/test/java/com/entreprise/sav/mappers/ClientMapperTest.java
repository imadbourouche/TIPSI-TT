package com.entreprise.sav.mappers;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.entities.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientMapperTest {

    @Test
    public void should_map_create_client_dto_to_entity() {
        CreateClientDto dto = new CreateClientDto("Test Client", "IT Sector");
        Client client = ClientMapper.toEntity(dto);

        assertNotNull(client);
        assertEquals("Test Client", client.name);
        assertEquals("IT Sector", client.sector);
    }
}
