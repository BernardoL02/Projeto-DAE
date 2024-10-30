package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.util.List;

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

    @GET
    @Path("encomendas/{id}/{username}")
    public Response getEncomendasClienteById(@PathParam("id") int id, @PathParam("username") String username) {
        var encomenda = clienteBean.findEncomendaById(username, id);
        return Response.ok(EncomendasDTO.from(encomenda)).build();
    }

    @GET
    @Path("encomendas/estado/{estado}/{username}")
    public Response getEncomendasClienteByEstado(@PathParam("estado") String estado, @PathParam("username") String username) {
        List<Encomenda> encomendas = clienteBean.findEncomendaByEstado(username, estado);
        return Response.ok( EncomendasDTO.from(encomendas)).build();
    }

    @PATCH
    @Path("encomendas/{id}/{username}")
    public Response cancelarEncomenda(@PathParam("id") int id, @PathParam("username") String username) {
        clienteBean.cancelarEncomenda(id,username);
        return Response.ok("Encomenda " + id + " cancelada com sucesso.").build();
    }

    @GET
    @Path("encomendas/{username}/sensor/{tipo_sensor}")
    public Response getUltimaLeituraSensores(@PathParam("tipo_sensor") String tipo_sensor, @PathParam("username") String username) {
        List<SensorDTO> sensores = clienteBean.getUltimaLeituraSensores(tipo_sensor, username);
        return Response.ok(sensores).build();
    }




}
