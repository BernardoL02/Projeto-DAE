package pt.ipleiria.estg.dei.ei.dea.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.AlertaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorAllDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorValorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.AlertaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.util.List;

@Path("sensor")
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ExternalSensorService {

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
        return sensorBean.updateValor(id, sensorDTO);
    }

    @PATCH
    @Path("/{id}/desativar")
    public Response updateEstado(@PathParam("id") int id, SensorDTO sensorDTO) {
        return sensorBean.updateEstado(id, sensorDTO);
    }

}
