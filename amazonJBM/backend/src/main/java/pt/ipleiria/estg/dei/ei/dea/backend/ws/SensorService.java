package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.AlertaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.util.List;

@Path("sensor")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class SensorService {

    @EJB
    private SensorBean sensorBean;

    @EJB
    private AlertaBean alertaBean;

    @GET
    @Path("/")
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
}
