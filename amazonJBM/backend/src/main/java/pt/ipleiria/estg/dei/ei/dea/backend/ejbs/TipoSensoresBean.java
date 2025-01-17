package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Stateless
public class TipoSensoresBean {

    @PersistenceContext
    private EntityManager em;

    public Response create(int id, String tipo) {

        Tipo_Sensores existingSensor = em.find(Tipo_Sensores.class, id);
        if (existingSensor != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe um tipo de sensor com o ID fornecido!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (em.createNamedQuery("existsTipoSensor", Long.class).setParameter("tipo", tipo).getSingleResult() > 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe um sensor deste tipo!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        }

        Tipo_Sensores tipoSensores = new Tipo_Sensores(id, tipo);

        em.persist(tipoSensores);

        return Response.ok("Tipo de sensor criado com sucesso.").build();
    }

    public Tipo_Sensores find(int id) {
        Tipo_Sensores tipo = em.find(Tipo_Sensores.class, id);
        return tipo;
    }

    public List<Tipo_Sensores> findAll() {
        return em.createNamedQuery("getTipoSensor", Tipo_Sensores.class).getResultList();
    }

}
