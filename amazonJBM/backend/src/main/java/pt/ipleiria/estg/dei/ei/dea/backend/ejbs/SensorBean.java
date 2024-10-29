package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager em;

    public void create(long id, int valor, long tipoId, String estado, int valMax, int valMin) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);
        if (tipoSensores == null) {
            throw new NoSuchElementException("Tipo_Sensores com ID " + tipoId + " não encontrado.");
        }
        var sensor = new Sensor(id, valor, tipoSensores, estado, valMax, valMin);
        em.persist(sensor);
    }
    public void create(long id, int valor, long tipoId, String estado) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);
        if (tipoSensores == null) {
            throw new NoSuchElementException("Tipo_Sensores com ID " + tipoId + " não encontrado.");
        }
        var sensor = new Sensor(id, valor, tipoSensores, estado);
        em.persist(sensor);
    }



}
