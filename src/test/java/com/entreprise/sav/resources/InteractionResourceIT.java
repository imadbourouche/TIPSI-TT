package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateClientDto;
import com.entreprise.sav.dto.CreateInteractionDto;
import com.entreprise.sav.enumeration.InteractionType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class InteractionResourceIT {

    private Integer testClientId;

    @BeforeEach
    public void setup() {
        // Create client directly via ReST to use for Interaction tests
        CreateClientDto dto = new CreateClientDto("Int Resource Client", "Sales");
        testClientId = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/api/clients")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void should_create_interaction() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.MEETING,
                "Discussion",
                LocalDateTime.now().minusDays(1),
                45,
                "Bob",
                testClientId.longValue()
        );

        given()
            .contentType(ContentType.JSON)
            .body(dto)
            .when()
            .post("/api/interactions")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("type", equalTo("MEETING"));
    }

    @Test
    public void should_list_interactions() {
        given()
            .when()
            .get("/api/interactions")
            .then()
            .statusCode(200);
    }


    @Test
    public void should_get_interaction_by_id() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.MEETING,
                "Discussion",
                LocalDateTime.now().minusDays(1),
                45,
                "Bob",
                testClientId.longValue()
        );

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post("/api/interactions")
            .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .when()
            .get("/api/interactions/" + id)
            .then()
            .statusCode(200)
            .body("type", equalTo("MEETING"));
    }

    @Test
    public void should_delete_interaction_by_id() {
        CreateInteractionDto dto = new CreateInteractionDto(
                InteractionType.MEETING,
                "Discussion",
                LocalDateTime.now().minusDays(1),
                45,
                "Bob",
                testClientId.longValue()
        );

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post("/api/interactions")
            .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .when()
            .delete("/api/interactions/" + id)
            .then()
            .statusCode(204);
    }
}
