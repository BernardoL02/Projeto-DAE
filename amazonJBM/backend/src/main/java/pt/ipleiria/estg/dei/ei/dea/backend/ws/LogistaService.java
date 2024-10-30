package pt.ipleiria.estg.dei.ei.dea.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

import java.util.ArrayList;
import java.util.List;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.LogistaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.LogistaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.awt.*;

@Path("sl")
 @Produces({MediaType.APPLICATION_JSON})
 @Consumes({MediaType.APPLICATION_JSON})

 public class LogistaService {

     @EJB
     private EncomendaBean encomendaBean;
     @GET
     @Path("encomendas/estado/{estado}")
     public Response getEncomendaByEstado(@PathParam("estado") String estado) {
         List<Encomenda> encomendas = encomendaBean.findEncomendasByEstado(estado);
         return Response.ok( EncomendasDTO.from(encomendas)).build();
     }

    @GET
    @Path("encomendas/{id}")
    public Response getEncomendasById(@PathParam("id") int id) {
        var encomenda = encomendaBean.find(id);
        return Response.ok(EncomendasDTO.from(encomenda)).build();
    }

    @PATCH
    @Path("encomendas/{id}/Entregue")
    public Response entregarEncomenda(@PathParam("id") int id) {
        encomendaBean.entregarEncomenda(id);
        return Response.ok("Encomenda " + id + " concluida com sucesso.").build();
    }

 }
