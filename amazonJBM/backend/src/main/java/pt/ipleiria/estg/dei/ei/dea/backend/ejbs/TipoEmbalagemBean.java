package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    public Response create(String tipo, List<TipoSensorDTO> tipoSensoresDTO) {

        if (em.createNamedQuery("existsTipoEmbalagem", Long.class).setParameter("tipo", tipo).getSingleResult() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Já existe uma embalagem deste tipo!").build();
        }

        List<Tipo_Sensores> tipoSensores = new ArrayList<>();

        for(TipoSensorDTO tipoSensor : tipoSensoresDTO){

            Tipo_Sensores tipoSensores1 = em.find(Tipo_Sensores.class, tipoSensor.getId());

            if(tipoSensores1 == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não encontrado!").build();
            }

            tipoSensores.add(tipoSensores1);
        }


        Tipo_Embalagem tipoEmbalagem = new Tipo_Embalagem(tipo,tipoSensores);
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
