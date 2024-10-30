package pt.ipleiria.estg.dei.ei.dea.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.util.List;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.LogistaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.LogistaBean;

import java.awt.*;

@Path("sl")
 @Produces({MediaType.APPLICATION_JSON})
 @Consumes({MediaType.APPLICATION_JSON})

 public class LogistaService {

     @EJB
     private EncomendaBean encomendaBean;
     @GET
     @Path("/encomendas/em-processamento")
     public List<EncomendasDTO> getAllEncomendasEmProcessamento() {
         return EncomendasDTO.from(encomendaBean.findAllEncomendasEmProcessamento());
     }
 }
