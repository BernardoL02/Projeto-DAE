package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.List;

@Stateless
public class AlertaBean {
    @PersistenceContext
    private EntityManager em;

    public void create(int id, String mensagem, int id_sensor, String valor, int id_volume){

        Volume volume = em.find(Volume.class, id_volume);
        Sensor sensor = em.find(Sensor.class, id_sensor);

        var alerta = new Alerta(id, mensagem, sensor, valor, volume);
        em.persist(alerta);
    }

    public List<Alerta> findAll() {
        return em.createNamedQuery("Alerta.findAll", Alerta.class).getResultList();
    }

}
