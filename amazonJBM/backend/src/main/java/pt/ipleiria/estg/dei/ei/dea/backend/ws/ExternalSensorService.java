package pt.ipleiria.estg.dei.ei.dea.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.AlertaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.LeituraBean;
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

    @EJB
    private LeituraBean leituraBean;

    @GET
    @Path("/")//Done
    public List<ResSensorAllDTO> getAllSensores() {
        return ResSensorAllDTO.from(sensorBean.findAll());
    }

    @POST
    @Path("/")//Done
    public Response create(LeituraDTO leituraDTO) {
        return leituraBean.create(leituraDTO.getId_sensor(),leituraDTO.getBateria(),leituraDTO.getValor());
    }
    
    @PATCH
    @Path("/{id}")//Done
    public Response updateEstado(@PathParam("id") int id) {
        return sensorBean.updateEstado(id);
    }

}
