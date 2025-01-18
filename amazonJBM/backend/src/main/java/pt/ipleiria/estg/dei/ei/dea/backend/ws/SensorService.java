package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.*;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("sensor")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@PermitAll
public class SensorService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    @EJB
    private UtilizadorBean utilizadorBean;

    @POST
    @Path("/tipo")
    @RolesAllowed({"Logista"})
    public Response createTipoSensor(CreateTipoSensorDTO createTipoSensorDTO) {
        return tipoSensoresBean.create(createTipoSensorDTO.getId(),createTipoSensorDTO.getTipo());
    }

    @GET
    @Path("/tipo")
    @RolesAllowed({"Cliente", "Gestor", "Logista"})
    public Response getTipoSensores() {
        List<Tipo_Sensores> tipoSensores = tipoSensoresBean.findAll();
        return Response.ok(ResTipoSensoresDTO.from(tipoSensores)).build();
    }

    @GET
    @Path("/{tipo_sensor}")
    @RolesAllowed({"Gestor", "Cliente"})
    public Response getUltimaLeituraSensoresByTipo(@PathParam("tipo_sensor") String tipo_sensor) {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        return sensorBean.getUltimaLeituraSensoresByTipo(tipo_sensor, user);
    }

    @GET
    @Path("/{id}/alerta")
    @RolesAllowed({"Gestor", "Cliente"})
    public Response getAlertasSensor(@PathParam("id") int id) {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        return sensorBean.getAlertasSensor(id, user);
    }

    @GET
    @Path("/{id}/leitura")
    @RolesAllowed({"Gestor", "Cliente"})
    public Response getLeiturasSensor(@PathParam("id") int id) {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        return sensorBean.getLeiturasSensor(id, user);
    }

}
