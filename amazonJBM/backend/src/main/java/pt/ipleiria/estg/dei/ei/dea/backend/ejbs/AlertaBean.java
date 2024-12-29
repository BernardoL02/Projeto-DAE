package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class AlertaBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private EncomendaBean encomendaBean;

    public void create(String mensagem, int id_sensor, String valor, int id_volume){

        Volume volume = em.find(Volume.class, id_volume);
        Sensor sensor = em.find(Sensor.class, id_sensor);

        var alerta = new Alerta(mensagem, sensor, valor, volume);
        em.persist(alerta);
    }

    public List<Alerta> findAll() {
        return em.createNamedQuery("Alerta.findAll", Alerta.class).getResultList();
    }

    public List<Alerta> getEncomendasAlertas(Utilizador user) {
        List<Encomenda> encomendasPorEntregar = encomendaBean.findEncomendasByEstado("PorEntregar", user);

        List<Integer> encomendaIds = encomendasPorEntregar.stream()
                .map(Encomenda::getId)
                .collect(Collectors.toList());

        List<Alerta> todosAlertas = findAll();
        return todosAlertas.stream()
                .filter(alerta -> encomendaIds.contains(alerta.getVolume().getEncomenda().getId()))
                .collect(Collectors.toList());
    }

    public List<Alerta> getAlertasEncomenda(int id) {
        List<Alerta> alertas = findAll();
        return alertas.stream()
                .filter(alerta -> alerta.getVolume().getEncomenda().getId() == id)
                .collect(Collectors.toList());
    }
}
