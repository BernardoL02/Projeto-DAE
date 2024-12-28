package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

@Path("volume")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class VolumeService {

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/{id}")
    public Response getDetalhesVolume(@PathParam("id") int id){
        Volume volume = volumeBean.find(id);

        return Response.ok(ResVolumeDetalhesDTO.from(volume, "SO")).build();
    }


}
