package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResClienteDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ClienteBean;

@Path("clientes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClientesService {

    @EJB
    private ClienteBean clienteBean;

    @GET
    @Path("/")
    public Response getClientes() {
        var clientes = clienteBean.findAll();
        return Response.ok(ResClienteDTO.from(clientes)).build();
    }
}
