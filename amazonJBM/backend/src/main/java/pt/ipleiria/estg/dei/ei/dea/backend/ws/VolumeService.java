package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorDetalhesDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResVolumeDetalhesDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.VolumeBean;

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
        var volume = volumeBean.find(id);

        System.out.println("Volume: " + volume);
        System.out.println("Encomenda: " + volume.getEncomenda());
        System.out.println("Volumes na Encomenda: " + volume.getEncomenda().getVolumes());
        System.out.println("Embalagens do Volume: " + volume.getEmbalagens());

        ResVolumeDetalhesDTO<ResSensorDetalhesDTO> volumeDTO = ResVolumeDetalhesDTO.from(volume,"SL");
        return Response.ok(volumeDTO).build();
    }


}
