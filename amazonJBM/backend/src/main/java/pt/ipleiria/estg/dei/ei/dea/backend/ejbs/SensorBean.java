package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorValorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String valor, int tipoId, String estado, int bateria, int valMax, int valMin, int id_volume) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);
        Volume volume = em.find(Volume.class, id_volume);
        if (tipoSensores == null) {
            throw new NoSuchElementException("Tipo_Sensores com ID " + tipoId + " não encontrado.");
        }
        var sensor = new Sensor(id, valor, tipoSensores, estado, bateria, valMax, valMin, volume);
        em.persist(sensor);
    }
    public void create(int id, String valor, int tipoId, String estado, int bateria, int id_volume) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);
        Volume volume = em.find(Volume.class, id_volume);
        if (tipoSensores == null) {
            throw new NoSuchElementException("Tipo_Sensores com ID " + tipoId + " não encontrado.");
        }
        var sensor = new Sensor(id, valor, tipoSensores, estado, bateria, volume);
        em.persist(sensor);
    }

    public List<Sensor> findAll() {
        return em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
    }


    public Sensor updateEstado(int id, SensorDTO sensorDTO) {
        Sensor sensor = em.find(Sensor.class, id);

        sensor.setEstado(sensorDTO.getEstado());

        em.merge(sensor);
        return sensor;// TODO: tratar possiveis erros de o sensor nao existir
    }

    public Sensor updateValor(int id, SensorDTO sensorDTO) {
        Sensor sensor = em.find(Sensor.class, id);

        sensor.setValor(sensorDTO.getValor());
        sensor.setBateria(sensorDTO.getBateria());

        em.merge(sensor);
        return sensor;// TODO: tratar possiveis erros de o sensor nao existir
    }
}
