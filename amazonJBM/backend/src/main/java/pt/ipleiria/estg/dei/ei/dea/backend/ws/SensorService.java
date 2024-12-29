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
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
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

    @GET
    @Path("tipos")
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
    @Path("/{id}/alertas")
    public Response getAlertasSensor(@PathParam("id") int id) {
        List<Alerta> alertas = sensorBean.getAlertasSensor(id);
        return Response.ok(alertas.stream().map(ResAlertasSensorDTO::from).collect(Collectors.toList())).build();
    }

}
