package com.entreprise.sav.mappers;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.entities.Client;

public class ClientMapper {
    public static Client toEntity(CreateClientDto dto) {
        Client client = new Client();
        client.name = dto.name();
        client.sector = dto.sector();
        return client;
    }
}
