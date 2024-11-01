package pt.ipleiria.estg.dei.ei.dea.backend.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Define quais domínios podem acessar
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*"); // Use "*" para todos ou "http://localhost:3000" para o domínio do Nuxt.

        // Define os métodos HTTP permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");

        // Define quais cabeçalhos podem ser usados na requisição
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");

        // Define se cookies e credenciais devem ser permitidos
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
    }
}
