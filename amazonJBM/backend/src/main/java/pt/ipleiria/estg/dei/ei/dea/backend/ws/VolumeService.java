package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.UtilizadorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

@Path("volume")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@PermitAll
public class VolumeService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UtilizadorBean utilizadorBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/{id}")
    public Response getDetalhesVolume(@PathParam("id") int id){
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        return volumeBean.verDetalhesVolume(id, user);
    }

    @PATCH
    @Path("/{id}")
    public Response updateEstado(@PathParam("id") int id) {
        return volumeBean.entregarVolume(id);
    }
}
