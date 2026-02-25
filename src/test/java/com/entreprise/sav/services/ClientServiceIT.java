package com.entreprise.sav.services;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.InteractionStatsDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.exceptions.NotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ClientServiceIT {

    @Inject
    ClientService clientService;

    @Test
    public void should_create_and_get_client() {
        CreateClientDto dto = new CreateClientDto("IT Corp", "Technology");
        Client created = clientService.createClient(dto);
        
        assertNotNull(created.id);
        assertEquals("IT Corp", created.name);
        assertEquals("Technology", created.sector);

        Client fetched = clientService.getClient(created.id);
        assertEquals(created.id, fetched.id);
        assertEquals("IT Corp", fetched.name);
    }

    @Test
    public void should_throw_exception_when_getting_non_existent_client() {
        assertThrows(NotFoundException.class, () -> {
            clientService.getClient(99999L);
        });
    }

    @Test
    public void should_list_all_clients() {
        CreateClientDto dto = new CreateClientDto("List Test", "Sector");
        clientService.createClient(dto);
        
        List<Client> clients = clientService.listClients();
        assertFalse(clients.isEmpty());
    }

    @Test
    public void should_delete_client() {
        CreateClientDto dto = new CreateClientDto("To Delete", "Sector");
        Client created = clientService.createClient(dto);
        
        clientService.deleteClient(created.id);
        
        Client deleted = clientService.getClient(created.id);
        assertNotNull(deleted.deletedAt);
    }

    @Test
    public void should_return_empty_client_stats() {
        CreateClientDto dto = new CreateClientDto("Stats Client", "Sector");
        Client created = clientService.createClient(dto);
        
        InteractionStatsDto stats = clientService.getClientStats(created.id);
        assertEquals(created.id, stats.client_id());
        assertEquals(0, stats.total_interactions());
        assertEquals(0, stats.total_duration_minutes());
    }
}
