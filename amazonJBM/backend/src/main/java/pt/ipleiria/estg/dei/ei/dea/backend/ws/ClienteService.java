package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ClienteBean;

@Path("sac") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ClienteService {

    @EJB
    private ClienteBean clienteBean;

    @GET
    @Path("encomendas/{username}")
    public Response getEncomendasCliente(@PathParam("username") String username) {
        var cliente = clienteBean.findWithEncomendas(username);
        return Response.ok(EncomendasDTO.from(cliente.getEncomendas())).build();
    }




}
