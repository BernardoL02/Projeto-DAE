package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.EmbalagemBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.TipoEmbalagemBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

import java.util.List;

@Path("embalagem")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Logista"})
public class EmbalagemService {

    @EJB
    SensorBean sensorBean;

    @EJB
    TipoEmbalagemBean tipoEmbalagemBean;

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

    @GET
    @Path("tipos")
    public Response getTiposEmbalagem() {
        List<Tipo_Embalagem> tipoEmbalagens = tipoEmbalagemBean.findAll();
        return Response.ok(ResTipoEmbalagemDTO.from(tipoEmbalagens)).build();
    }

    @POST
    @Path("/tipo")
    public Response criarTipoEmbalagem(CreateTipoEmbalagemDTO createTipoEmbalagemDTO) {
        return tipoEmbalagemBean.create(createTipoEmbalagemDTO.getId(),createTipoEmbalagemDTO.getTipo(), createTipoEmbalagemDTO.getTipos_sensores());
    }
}
