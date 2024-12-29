package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.AlertaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.GestorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.TipoSensoresBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensor")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
@Authenticated
@PermitAll
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    @EJB
    private AlertaBean alertaBean;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    @EJB
    private GestorBean gestorBean;

    @GET
    @Path("/")
    @RolesAllowed({"Gestor"})
    public List<ResSensorAllDTO> getAllSensores() {
        return ResSensorAllDTO.from(sensorBean.findAll());
    }

    @POST
    @Path("/")
    public Response create(AlertaDTO alertaDTO) {
        alertaBean.create(
                alertaDTO.getMensagem(),
                alertaDTO.getId_sensor(),
                alertaDTO.getValor(),
                alertaDTO.getId_volume()
        );

        return Response.ok("Alerta criado com sucesso").build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateValor(@PathParam("id") int id, SensorDTO sensorDTO) {
        var updatedSensor = sensorBean.updateValor(id, sensorDTO);
        return Response.ok(ResSensorValorDTO.from(updatedSensor)).build();
    }

   @PATCH
    @Path("/{id}/desativar")
    public Response updateEstado(@PathParam("id") int id, SensorDTO sensorDTO) {
        Sensor updatedSensor = sensorBean.updateEstado(id, sensorDTO);
        return Response.ok(SensorDTO.from(updatedSensor)).build();
    }

    @GET
    @Path("tipos")
    @RolesAllowed({"Gestor", "Logista"})
    public Response getTipoSensores() {
        List<Tipo_Sensores> tipoSensores = tipoSensoresBean.findAll();
        return Response.ok(ResTipoSensoresDTO.from(tipoSensores)).build();
    }

    @GET
    @Path("/{tipo_sensor}")
    public Response getUltimaLeituraSensoresByTipo(@PathParam("tipo_sensor") String tipo_sensor) {
        List<Sensor> alertas = sensorBean.getUltimaLeituraSensoresByTipo(tipo_sensor);
        return Response.ok(alertas.stream().map(ResSensorUltimaLeituraByTipoDTO::from).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}/alertas")
    public Response getAlertasSensor(@PathParam("id") int id) {
        List<Alerta> alertas = sensorBean.getAlertasSensor(id);
        return Response.ok(alertas.stream().map(ResAlertasSensorDTO::from).collect(Collectors.toList())).build();
    }

}
