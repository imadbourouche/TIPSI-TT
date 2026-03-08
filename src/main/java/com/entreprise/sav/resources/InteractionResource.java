package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.dto.InteractionFilter;
import com.entreprise.sav.dto.InteractionResponseDto;
import com.entreprise.sav.dto.UpdateInteractionDto;
import com.entreprise.sav.services.InteractionService;
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



@Path("/api/interactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InteractionResource {

    @Inject
    InteractionService interactionService;

    @GET
    @Operation(summary = "Endpoint pour lister les interactions")
    @APIResponse(responseCode = "200", description = "Interactions trouvées", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionResponseDto.class, type = ARRAY)))
    public Response listInteractions(@BeanParam InteractionFilter filter) {
        return Response.ok(interactionService.listInteractions(filter)).build();
    }

    @GET
    @Operation(summary = "Endpoint pour obtenir une interaction")
    @APIResponse(responseCode = "200", description = "Interaction trouvée", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionResponseDto.class, type = OBJECT)))
    @APIResponse(responseCode = "404", description = "Interaction introuvable")
    @Path("/{id}")
    public Response getInteraction(@PathParam("id") Long id) {
        return Response.ok(interactionService.getInteractionResponseDto(id)).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Endpoint pour créer une interaction")
    @APIResponse(responseCode = "201", description = "Interaction créée", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionResponseDto.class, type = OBJECT)))
    @APIResponse(responseCode = "400", description = "Interaction invalide")
    public Response createInteraction(@Valid CreateInteractionDto dto) {
        return Response.status(Response.Status.CREATED)
                .entity(interactionService.createInteraction(dto))
                .build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Endpoint pour mettre à jour une interaction")
    @APIResponse(responseCode = "200", description = "Interaction mise à jour", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = InteractionResponseDto.class, type = OBJECT)))
    @APIResponse(responseCode = "404", description = "Interaction introuvable")
    public Response updateInteraction(@PathParam("id") Long id, @Valid UpdateInteractionDto dto) {
        return Response.ok(interactionService.updateInteraction(id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Endpoint pour supprimer une interaction")
    @APIResponse(responseCode = "204", description = "Interaction supprimée")
    @APIResponse(responseCode = "404", description = "Interaction introuvable")
    public Response deleteInteraction(@PathParam("id") Long id) {
        interactionService.deleteInteraction(id);
        return Response.noContent().build();
    }
}
