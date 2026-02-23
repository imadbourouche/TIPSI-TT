package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.InteractionStatsDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.services.ClientService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject
    ClientService clientService;

    @GET
    public List<Client> listClients() {
        return clientService.listClients();
    }

    @POST
    @Transactional
    public Response createClient(@Valid CreateClientDto dto) {
        Client client = clientService.createClient(dto);
        return Response.status(Response.Status.CREATED).entity(client).build();
    }

    @GET
    @Path("/{id}")
    public Response getClient(@PathParam("id") Long id) {
        return Response.ok(clientService.getClient(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteClient(@PathParam("id") Long id) {
        clientService.deleteClient(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/stats")
    public Response getClientStats(@PathParam("id") Long id) {
        return Response.ok(clientService.getClientStats(id)).build();
    }

    @GET
    @Path("/{id}/interactions")
    public Response getClientInteractions(@PathParam("id") Long id) {
        return Response.ok(clientService.getClientInteractions(id)).build();
    }
}
