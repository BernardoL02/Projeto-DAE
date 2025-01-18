package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.UtilizadorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.UtilizadorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dea.backend.security.TokenIssuer;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {
    @Inject
    private TokenIssuer issuer;

    @EJB
    private UtilizadorBean userBean;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = issuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UtilizadorDTO.from(user)).build();
    }
}