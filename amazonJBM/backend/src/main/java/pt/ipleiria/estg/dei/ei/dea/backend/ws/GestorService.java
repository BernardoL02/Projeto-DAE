package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.GestorBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.util.List;

@Path("so") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class GestorService {

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private GestorBean gestorBean;

    @GET
    @Path("encomendas/pendentes")
    public Response getEncomendasPendendes() {
        List<Encomenda> encomendas = encomendaBean.findePendentes();
        return Response.ok( EncomendasDTO.from(encomendas)).build();
    }

    @GET
    @Path("encomendas/{estado}")
    public Response getEncomendaByEstado(@PathParam("estado") String estado) {
        List<Encomenda> encomendas = encomendaBean.findEncomendasByEstado(estado);
        return Response.ok( EncomendasDTO.from(encomendas)).build();
    }

    @PATCH
    @Path("encomendas/{id}/")
    public Response cancelarEncomenda(@PathParam("id") int id) {
        gestorBean.cancelarEncomenda(id);
        return Response.ok("Encomenda " + id + " cancelada com sucesso.").build();
    }

    @GET
    @Path("encomendas/{id}/")
    public Response getEncomendaById(@PathParam("id") int id) {
        Encomenda encomenda = encomendaBean.find(id);
        return Response.ok( EncomendasDTO.from(encomenda)).build();
    }
}
