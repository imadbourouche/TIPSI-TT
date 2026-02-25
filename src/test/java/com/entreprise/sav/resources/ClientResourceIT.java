package com.entreprise.sav.resources;

import com.entreprise.sav.dto.CreateClientDto;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestHTTPEndpoint(ClientResource.class)
public class ClientResourceIT {

    @Test
    public void should_create_client() {
        CreateClientDto dto = new CreateClientDto("Resource Client", "Tech");

        given()
            .contentType(ContentType.JSON)
            .body(dto)
            .when()
            .post()
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Resource Client"))
            .body("sector", equalTo("Tech"));
    }

    @Test
    public void should_list_clients() {
        given()
            .when()
            .get()
            .then()
            .statusCode(200);
    }

    @Test
    public void should_get_client_by_id() {
        CreateClientDto dto = new CreateClientDto("Single Client", "Retail");
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .when()
            .get("/" + id)
            .then()
            .statusCode(200)
            .body("name", equalTo("Single Client"));
    }

    @Test
    public void should_delete_client_by_id() {
        CreateClientDto dto = new CreateClientDto("Delete Client", "Retail");
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(dto)
            .post()
            .then()
            .statusCode(201)
            .extract().path("id");

        given()
            .when()
            .delete("/" + id)
            .then()
            .statusCode(204);
    }
}
