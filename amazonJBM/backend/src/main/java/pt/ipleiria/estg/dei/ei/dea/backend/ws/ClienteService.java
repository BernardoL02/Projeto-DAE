package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.AlertaSAC_DTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorUltimaLeituraByTipoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
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
    @Path("encomendas")
    public Response getEncomendasCliente() {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido

        var cliente = clienteBean.findWithEncomendas(username);
        return Response.ok(EncomendasDTO.from(cliente.getEncomendas())).build();
    }

    @GET
    @Path("encomendas/{id}")
    public Response getEncomendasClienteById(@PathParam("id") int id) {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido

        var encomenda = clienteBean.findEncomendaById(id);
        return Response.ok(EncomendasDTO.from(encomenda)).build();
    }

    @GET
    @Path("encomendas/estado/{estado}")
    public Response getEncomendasClienteByEstado(@PathParam("estado") String estado) {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido

        List<Encomenda> encomendas = clienteBean.findEncomendaByEstado(username, estado);
        return Response.ok(EncomendasDTO.from(encomendas)).build();
    }

    @PATCH
    @Path("encomendas/{id}")
    public Response cancelarEncomenda(@PathParam("id") int id) {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido e se a encomenda é dele

        clienteBean.cancelarEncomenda(id,username);
        return Response.ok("Encomenda " + id + " cancelada com sucesso.").build();
    }

    @GET
    @Path("encomendas/sensor/{tipo_sensor}")
    public Response getUltimaLeituraSensoresByTipo(@PathParam("tipo_sensor") String tipo_sensor) {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido e se a encomenda é dele

        List<Sensor> sensores = clienteBean.getUltimaLeituraSensoresByTipo(tipo_sensor, username);
        return Response.ok(ResSensorUltimaLeituraByTipoDTO.from(sensores)).build();
    }

    @GET
    @Path("encomendas/{id}/alertas")
    public Response getAlertasByEncomenda(@PathParam("id") int id) {

        String username = "Bernardo"; //Todo ver que user esta a fazer o pedido e se a encomenda é dele

        List<Alerta> alertas = clienteBean.getAlertasByEncomenda(id, username);
        return Response.ok(AlertaSAC_DTO.from(alertas)).build();
    }

    
    
}
