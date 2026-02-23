package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.services.InteractionService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/api/interactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InteractionResource {

    @Inject
    InteractionService interactionService;

    @GET
    public Response listInteractions(
            @QueryParam("client_id") Long clientId,
            @QueryParam("type") String type,
            @QueryParam("commercial") String commercial,
            @QueryParam("from") String from,
            @QueryParam("to") String to) {

        return Response.ok(interactionService.listInteractions(clientId, type, commercial, from, to)).build();
    }

    @GET
    @Path("/{id}")
    public Response getInteraction(@PathParam("id") Long id) {
        return Response.ok(interactionService.getInteractionResponseDto(id)).build();
    }

    @POST
    @Transactional
    public Response createInteraction(@Valid CreateInteractionDto dto) {
        return Response.status(Response.Status.CREATED)
                .entity(interactionService.createInteraction(dto))
                .build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response updateInteraction(@PathParam("id") Long id, @Valid UpdateInteractionDto dto) {
        return Response.ok(interactionService.updateInteraction(id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteInteraction(@PathParam("id") Long id) {
        interactionService.deleteInteraction(id);
        return Response.noContent().build();
    }
}
