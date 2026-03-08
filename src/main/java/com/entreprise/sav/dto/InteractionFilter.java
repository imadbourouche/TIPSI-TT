package com.entreprise.sav.dto;

import org.jboss.resteasy.reactive.RestQuery;

public record InteractionFilter(
    @RestQuery Long client_id, 
    @RestQuery String type, 
    @RestQuery String commercial, 
    @RestQuery String from, 
    @RestQuery String to
) {}