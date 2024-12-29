package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ProdutoBean;

@Path("produtos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class ProdutosService {

    @EJB
    private ProdutoBean produtoBean;

    @GET
    @Path("/")
    public Response getProdutos() {
        var produtos = produtoBean.findAll();
        return Response.ok(ProdutoDTO.from(produtos)).build();
    }
}
