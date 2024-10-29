package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Stateless
public class TipoSensoresBean {

    @PersistenceContext
    private EntityManager em;

    public void create(long id, String tipo) {
        var tipoSensores = new Tipo_Sensores(id, tipo);
        em.persist(tipoSensores);
    }

    public Tipo_Sensores find(long id) {
        var tipo = em.find(Tipo_Sensores.class, id);
        if (tipo == null) {
            throw new NoSuchElementException("Tipo_Sensores com ID " + id + " não encontrado.");
        }
        return tipo;
    }

}
