package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResCategoriaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.CategoriaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

@Path("categoria")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Logista"})
public class CategoriaService {

    @EJB
    private CategoriaBean categoriaBean;

    @GET
    @Path("/")
    public Response getCategorias() {;
        return Response.ok(ResCategoriaDTO.from(categoriaBean.findAll())).build();
    }
}
