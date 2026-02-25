import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @Context
    UriInfo uriInfo;

    @GET
    public Map<String, Object> root() {
        String baseUri = uriInfo.getBaseUri().toString();

        return Map.of(
                "service", "SAV Management API",
                "version", "1.0.0",
                "endpoints", List.of(
                        Map.of(
                            "url", baseUri + "q/swagger-ui", 
                            "description", "Swagger UI documentation"
                        ),
                        Map.of(
                            "url", baseUri + "api/clients", 
                            "description", "Client management"
                        ),
                        Map.of(
                            "url", baseUri + "api/interactions", 
                            "description", "Interaction management"
                        )
                )
        );
    }
}