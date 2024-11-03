package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Stateless
public class GestorBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private AlertaBean alertaBean;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    public void create(String username, String password, String email, String nome){
        var gestor = new Gestor(username, password, email, nome);
        em.persist(gestor);
    }

    public void cancelarEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id); //Todo -> Ver se encomenda existe e ver se esta em processamento se nao nao e possivel atualizar

        encomenda.setEstado("Cancelada");
        em.merge(encomenda);
    }

    public List<Alerta> getAlertasSensor(int sensorId) {
        List<Alerta> todosAlertas = alertaBean.findAll();
        return todosAlertas.stream().filter(alerta -> alerta.getSensor().getId() == sensorId).collect(Collectors.toList());
    }

    public List<Alerta> getAlertasEncomenda(int id) {
        List<Alerta> alertas = alertaBean.findAll();
        return alertas.stream()
                .filter(alerta -> alerta.getVolume().getEncomenda().getId() == id)
                .collect(Collectors.toList());
    }

    public List<Alerta> getEncomendasAlertas() {
        List<Encomenda> encomendasPorEntregar = encomendaBean.findEncomendasByEstado("PorEntregar");

        List<Integer> encomendaIds = encomendasPorEntregar.stream()
                .map(Encomenda::getId)
                .collect(Collectors.toList());

        List<Alerta> todosAlertas = alertaBean.findAll();
        return todosAlertas.stream()
                .filter(alerta -> encomendaIds.contains(alerta.getVolume().getEncomenda().getId()))
                .collect(Collectors.toList());
    }

    public List<Sensor> getUltimaLeituraSensoresByTipo(String tipo_sensor) {
        Tipo_Sensores tipoSensores = tipoSensoresBean.findAll().stream()
                .filter(tipo -> tipo.getTipo().equals(tipo_sensor))
                .findFirst().orElse(null);

        return em.createNamedQuery("Sensor.findByTipoAndEstado", Sensor.class).setParameter("tipoId", tipoSensores.getId()).getResultList();
    }

    public List<Object[]> getCoordenadasEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id);

        List<Object[]> coordenadasList = new ArrayList<>();

        for (Volume volume : encomenda.getVolumes()) {
            for (Sensor sensor : volume.getSensores()) {
                if ("GPS".equals(sensor.getTipo().getTipo())) {
                    coordenadasList.add(new Object[]{volume.getId(), volume.getProduto().getNome(), sensor.getValor()});
                }
            }
        }

        return coordenadasList;
    }
}
