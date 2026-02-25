package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.InteractionStatsDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.entities.Client;
import com.entreprise.sav.services.ClientService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.OBJECT;

import java.util.List;

@Path("/api/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject
    ClientService clientService;

    @GET
    @Operation(summary = "Endpoint pour lister tous les clients")
    @APIResponse(responseCode = "200", description = "Liste des clients", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Client.class, type = ARRAY)))
    public List<Client> listClients() {
        return clientService.listClients();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Endpoint pour obtenir un client")
    @APIResponse(responseCode = "200", description = "Client trouvé", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Client.class, type = OBJECT)))
    @APIResponse(responseCode = "404", description = "Client introuvable")
    public Response getClient(@PathParam("id") Long id) {
        return Response.ok(clientService.getClient(id)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Endpoint pour créer un client")
    @APIResponse(responseCode = "201", description = "Client créé", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Client.class, type = OBJECT)))
    @APIResponse(responseCode = "400", description = "Données invalides")
    public Response createClient(@Valid CreateClientDto dto) {
        Client client = clientService.createClient(dto);
        return Response.status(Response.Status.CREATED).entity(client).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Endpoint pour supprimer un client")
    @APIResponse(responseCode = "204", description = "Client supprimé")
    @APIResponse(responseCode = "404", description = "Client introuvable")
    public Response deleteClient(@PathParam("id") Long id) {
        clientService.deleteClient(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/stats")
    @Operation(summary = "Endpoint pour obtenir les stats d'un client")
    @APIResponse(responseCode = "200", description = "Stats trouvées", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionStatsDto.class, type = OBJECT)))
    @APIResponse(responseCode = "404", description = "Client introuvable")
    public Response getClientStats(@PathParam("id") Long id) {
        return Response.ok(clientService.getClientStats(id)).build();
    }

    @GET
    @Path("/{id}/interactions")
    @Operation(summary = "Endpoint pour obtenir les interactions d'un client")
    @APIResponse(responseCode = "200", description = "Interactions trouvées", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionResponseDto.class, type = ARRAY)))
    @APIResponse(responseCode = "404", description = "Client introuvable")
    public Response getClientInteractions(@PathParam("id") Long id) {
        return Response.ok(clientService.getClientInteractions(id)).build();
    }
}
