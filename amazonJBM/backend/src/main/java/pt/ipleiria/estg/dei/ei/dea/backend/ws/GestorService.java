package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.GestorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.util.List;
import java.util.stream.Collectors;

@Path("so") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class GestorService {

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private GestorBean gestorBean;

    @GET
    @Path("encomendas/pendentes")
    public Response getEncomendasPendendes() {
        List<Encomenda> encomendas = encomendaBean.findPendentes();
        return Response.ok( ResEncomendaEstadoDTO.from(encomendas)).build();
    }

    @GET
    @Path("encomendas/{estado}")
    public Response getEncomendaByEstado(@PathParam("estado") String estado) {
        List<Encomenda> encomendas = encomendaBean.findEncomendasByEstado(estado);
        return Response.ok( ResEncomendaEstadoDTO.from(encomendas)).build();
    }

    @PATCH
    @Path("encomendas/{id}/")
    public Response cancelarEncomenda(@PathParam("id") int id) {
        gestorBean.cancelarEncomenda(id);
        return Response.ok("Encomenda " + id + " cancelada com sucesso.").build();
    }

    @GET
    @Path("encomendas/detalhes/{id}/")
    public Response getEncomendaById(@PathParam("id") int id) {
        Encomenda encomenda = encomendaBean.find(id);
        return Response.ok( EncomendasDTO.from(encomenda)).build();
    }

    @GET
    @Path("sensor/{id}/alertas")
    public Response getAlertasSensor(@PathParam("id") int id) {
        List<Alerta> alertas = gestorBean.getAlertasSensor(id);
        return Response.ok(alertas.stream().map(ResAlertasSensorDTO::from).collect(Collectors.toList())).build();
    }

    @GET
    @Path("encomendas/{id}/alertas")
    public Response getAlertasEncomenda(@PathParam("id") int id) {
        List<Alerta> alertas = gestorBean.getAlertasEncomenda(id);
        ResAlertasEncomendaDTO responseDto = ResAlertasEncomendaDTO.from(alertas);
        return Response.ok(responseDto).build();
    }

    @GET
    @Path("encomendas/alertas")
    public Response getEncomendasAlertas() {
        List<Alerta> alertas = gestorBean.getEncomendasAlertas();
        return Response.ok(alertas.stream().map(ResEncomendasAlertasDTO::from).collect(Collectors.toList())).build();
    }

    @GET
    @Path("sensor/{tipo_sensor}")
    public Response getUltimaLeituraSensoresByTipo(@PathParam("tipo_sensor") String tipo_sensor) {
        List<Sensor> alertas = gestorBean.getUltimaLeituraSensoresByTipo(tipo_sensor);
        return Response.ok(alertas.stream().map(ResSensorUltimaLeituraByTipoDTO::from).collect(Collectors.toList())).build();
    }

}
