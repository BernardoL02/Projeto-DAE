package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResAlertasSensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorUltimaLeituraByTipoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResSensorValorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Stateless
public class SensorBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    @EJB
    private AlertaBean alertaBean;

    public Response create(String valor, int tipoId, int bateria, int valMax, int valMin, int id_embalagem) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);

        if (tipoSensores == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não encontrado!").build();
        }

        Embalagem embalagem = em.find(Embalagem.class, id_embalagem);

        if (embalagem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Embalagem não encontrada!").build();
        }

        Volume volume = em.find(Volume.class,embalagem.getVolume().getId());

        if(!volume.getEncomenda().getEstado().equals("EmProcessamento")){
            return Response.status(Response.Status.NOT_FOUND).entity("Só é possível associar sensores a encomendas que estejam 'Em Processamento'.").build();
        }

        Sensor sensor = new Sensor(valor, tipoSensores, "ativo", bateria, valMax, valMin, embalagem);
        em.persist(sensor);

        embalagem.addSensor(sensor);

        return Response.ok("Sensor associado com sucesso").build();
    }

    public Response create(String valor, int tipoId, int bateria, int id_embalagem) {
        Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, tipoId);

        if (tipoSensores == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não encontrado!").build();
        }

        Embalagem embalagem = em.find(Embalagem.class, id_embalagem);

        if (embalagem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Embalagem não encontrada!").build();
        }

        Volume volume = em.find(Volume.class,embalagem.getVolume().getId());

        if(!volume.getEncomenda().getEstado().equals("EmProcessamento")){
            return Response.status(Response.Status.NOT_FOUND).entity("Só é possível associar sensores a encomendas que estejam 'Em Processamento'.").build();
        }

        Sensor sensor = new Sensor(valor, tipoSensores, "ativo", bateria, embalagem);
        em.persist(sensor);

        embalagem.addSensor(sensor);

        return Response.ok("Sensor associado com sucesso").build();
    }

    public List<Sensor> findAll() {
        return em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
    }

    public Sensor find(int id) {
        return em.find(Sensor.class, id);
    }

    public Response updateEstado(int id, SensorDTO sensorDTO) {
        Sensor sensor = em.find(Sensor.class, id);

        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor não encontrado!").build();
        }

        if(sensor.getEstado().equals("inativo")){
            return Response.status(Response.Status.NOT_FOUND).entity("Só se podem desativar sensores que estejam ativos!").build();
        }

        sensor.setEstado(sensorDTO.getEstado());
        em.merge(sensor);

        return Response.ok(ResSensorValorDTO.from(sensor)).build();
    }

    public Response updateValor(int id, SensorDTO sensorDTO) {
        Sensor sensor = this.find(id);

        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor não encontrado!").build();
        }

        sensor.setValor(sensorDTO.getValor());
        sensor.setBateria(sensorDTO.getBateria());

        em.merge(sensor);
        return Response.ok(ResSensorValorDTO.from(sensor)).build();
    }

    public Response getUltimaLeituraSensoresByTipo(String tipo_sensor, Utilizador user) {

        Tipo_Sensores tipoSensores = tipoSensoresBean.findAll().stream()
                .filter(tipo -> tipo.getTipo().equals(tipo_sensor))
                .findFirst().orElse(null);

        if(tipoSensores == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de Sensor não existe!").build();
        }

        List<Sensor> sensores = new ArrayList<>();

        if(user.isCliente()){
            sensores = em.createNamedQuery("Sensor.findByTipoAndEstadoByCliente", Sensor.class).setParameter("tipoId", tipoSensores.getId()).setParameter("username", user.getUsername()).getResultList();
        }else{
             sensores = em.createNamedQuery("Sensor.findByTipoAndEstado", Sensor.class).setParameter("tipoId", tipoSensores.getId()).getResultList();
        }

        return Response.ok(sensores.stream().map(ResSensorUltimaLeituraByTipoDTO::from).collect(Collectors.toList())).build();
    }

    public Response getAlertasSensor(int sensorId, Utilizador user) {

        Sensor sensor = this.find(sensorId);

        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor não encontrado!").build();
        }

        if(user.isCliente() && !sensor.getEmbalagem().getVolume().getEncomenda().getCliente().getUsername().equals(user.getUsername())){
            return Response.status(Response.Status.NOT_FOUND).entity("Apenas pode ver alertas de sensores que lhe pertencem.").build();
        }

        List<Alerta> alertas = em.createNamedQuery("Alerta.findAllByCliente", Alerta.class).setParameter("sensor_id", sensorId).getResultList();

        return Response.ok(alertas.stream().map(ResAlertasSensorDTO::from).collect(Collectors.toList())).build();
    }
}
