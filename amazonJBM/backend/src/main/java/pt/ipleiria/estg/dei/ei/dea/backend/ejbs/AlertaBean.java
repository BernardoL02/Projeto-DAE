package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResAlertasEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class AlertaBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private EncomendaBean encomendaBean;

    public void create(String mensagem, int id_sensor, String valor,int bateria,int id_volume){

        Volume volume = em.find(Volume.class, id_volume);
        Sensor sensor = em.find(Sensor.class, id_sensor);

        var alerta = new Alerta(mensagem, sensor, valor,bateria, volume);
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

    public Response getAlertasEncomenda(int id, Utilizador user) {
        Encomenda encomenda = encomendaBean.find(id);

        if(encomenda == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Encomenda não encontrada!").build();
        }

        if(user.isCliente() && !encomenda.getCliente().getUsername().equals(user.getUsername())){
            return Response.status(Response.Status.NOT_FOUND).entity("Apenas pode ver alertas das suas encomendas!").build();
        }

        List<Alerta> alertas = em.createNamedQuery("getAlertasByEncomendaId", Alerta.class)
                .setParameter("encomendaId", encomenda.getId())
                .getResultList();

        return Response.ok(ResAlertasEncomendaDTO.from(alertas)).build();

    }
}
