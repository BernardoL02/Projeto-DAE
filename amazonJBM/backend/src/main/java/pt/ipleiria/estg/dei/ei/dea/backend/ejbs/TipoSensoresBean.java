package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Stateless
public class TipoSensoresBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String tipo) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, id);

        if (tipoSensores == null) {
            tipoSensores = new Tipo_Sensores(id, tipo);
            em.persist(tipoSensores);
        } else {

            tipoSensores.setTipo(tipo);
            em.merge(tipoSensores);
        }
    }

    public Tipo_Sensores find(int id) {
        var tipo = em.find(Tipo_Sensores.class, id);

        return tipo;
    }

    public List<Tipo_Sensores> findAll() {
        return em.createNamedQuery("getTipoSensor", Tipo_Sensores.class).getResultList();
    }

}
