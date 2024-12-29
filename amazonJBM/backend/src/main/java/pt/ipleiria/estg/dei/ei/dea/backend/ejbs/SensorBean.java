package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorValorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    @EJB
    private AlertaBean alertaBean;

    public Response create(String valor, int tipoId, String estado, int bateria, int valMax, int valMin, int id_embalagem) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);

        if (tipoSensores == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não encontrado!").build();
        }

        Embalagem embalagem = em.find(Embalagem.class, id_embalagem);

        if (embalagem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Embalagem não encontrada!").build();
        }

        var sensor = new Sensor(valor, tipoSensores, estado, bateria, valMax, valMin, embalagem);
        em.persist(sensor);
        return Response.ok("Sensor associado com sucesso").build();
    }

    public Response create(String valor, int tipoId, String estado, int bateria, int id_embalagem) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);

        if (tipoSensores == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não encontrado!").build();
        }

        Embalagem embalagem = em.find(Embalagem.class, id_embalagem);

        if (embalagem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Embalagem não encontrada!").build();
        }

        var sensor = new Sensor(valor, tipoSensores, estado, bateria, embalagem);
        em.persist(sensor);
        return Response.ok("Sensor associado com sucesso").build();
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

        //sensor.setValor(sensorDTO.getValor());
        //sensor.setBateria(sensorDTO.getBateria());

        em.merge(sensor);
        return sensor;// TODO: tratar possiveis erros de o sensor nao existir
    }

    public List<Sensor> getUltimaLeituraSensoresByTipo(String tipo_sensor) {
        Tipo_Sensores tipoSensores = tipoSensoresBean.findAll().stream()
                .filter(tipo -> tipo.getTipo().equals(tipo_sensor))
                .findFirst().orElse(null);

        return em.createNamedQuery("Sensor.findByTipoAndEstado", Sensor.class).setParameter("tipoId", tipoSensores.getId()).getResultList();
    }

    public List<Alerta> getAlertasSensor(int sensorId) {
        List<Alerta> todosAlertas = alertaBean.findAll();
        return todosAlertas.stream().filter(alerta -> alerta.getSensor().getId() == sensorId).collect(Collectors.toList());
    }
}
