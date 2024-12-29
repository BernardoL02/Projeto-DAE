package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

@Path("embalagem")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Logista"})
public class EmbalagemService {

    @EJB
    SensorBean sensorBean;

    @POST
    @Path("/{id}/sensor")
    public Response associarSensorAEmbalagem(@PathParam("id") int id, SensorDTO sensorDTO) {

        if(sensorDTO.getTipoId() == 4){
            return sensorBean.create(
                    sensorDTO.getValor(),
                    sensorDTO.getTipoId(),
                    sensorDTO.getBateria(),
                    id
            );
        }

           return sensorBean.create(
                    sensorDTO.getValor(),
                    sensorDTO.getTipoId(),
                    sensorDTO.getBateria(),
                    sensorDTO.getValMax(),
                    sensorDTO.getValMin(),
                    id
            );
    }
}
