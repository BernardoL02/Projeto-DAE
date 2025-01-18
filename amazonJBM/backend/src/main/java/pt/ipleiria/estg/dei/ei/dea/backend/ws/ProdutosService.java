package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

import java.util.List;

@Path("produto")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Logista"})
public class ProdutosService {

    @EJB
    private ProdutoBean produtoBean;

    @GET
    @Path("/")//Done
    public Response getProdutos() {
        return Response.ok(ProdutoDTO.from(produtoBean.findAll())).build();
    }

    @POST
    @Path("/") //Done
    public Response criarProduto(CreateProdutoDTO createPrdoutoDTO) {
        return produtoBean.create(createPrdoutoDTO.getId(),createPrdoutoDTO.getNome(),createPrdoutoDTO.getId_categoria());
    }
}
