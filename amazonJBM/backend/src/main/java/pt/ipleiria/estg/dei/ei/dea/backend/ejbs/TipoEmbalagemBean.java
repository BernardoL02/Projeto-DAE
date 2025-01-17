package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResTipoEmbalagemDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResTipoSensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.TipoSensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class TipoEmbalagemBean {

    @PersistenceContext
    private EntityManager em;

    public Response create(int id, String tipo, List<TipoSensorDTO> tipoSensoresDTO) {

        Tipo_Embalagem existingTipoEmbalagem = em.find(Tipo_Embalagem.class, id);
        if (existingTipoEmbalagem != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe um tipo de embalagem com o ID fornecido!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (em.createNamedQuery("existsTipoEmbalagem", Long.class).setParameter("tipo", tipo).getSingleResult() > 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe uma embalagem deste tipo!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        List<Tipo_Sensores> tipoSensores = new ArrayList<>();

        for(TipoSensorDTO tipoSensor : tipoSensoresDTO){

            Tipo_Sensores tipoSensores1 = em.find(Tipo_Sensores.class, tipoSensor.getId());

            if(tipoSensores1 == null){
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Tipo de Sensor não encontrado!\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            tipoSensores.add(tipoSensores1);
        }


        Tipo_Embalagem tipoEmbalagem = new Tipo_Embalagem(id,tipo,tipoSensores);
        em.persist(tipoEmbalagem);

        return Response.ok("Tipo de embalagem criado com sucesso.").build();
    }

    public Tipo_Embalagem find(int id) {
        return  em.find(Tipo_Embalagem.class, id);
    }

    public List<Tipo_Embalagem> findAll() {
        return  em.createNamedQuery("getTipoEmbalagem", Tipo_Embalagem.class).getResultList();
    }
}
