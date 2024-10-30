package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EncomendaBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClienteBean clienteBean;

    public void create(int id, String client_username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega) {

        Cliente cliente = clienteBean.find(client_username);

        Encomenda encomenda = new Encomenda(id,cliente,estado,data_expedicao,data_entrega);
        em.persist(encomenda);
    }

    public Encomenda find(int id) {
        var encomenda = em.find(Encomenda.class, id);
        if (encomenda == null) {
            throw new NoSuchElementException("Encomenda com ID " + id + " não encontrado.");
        }
        Hibernate.initialize(encomenda);
        return encomenda;
    }

    public List<Encomenda> findEncomendasByEstado(String estado) {
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        for (Encomenda encomenda : todasEncomendas) {
            if (encomenda.getEstado().equals(estado)){
                Hibernate.initialize(encomenda.getVolumes());
                encomendasFiltradas.add(encomenda);
            }
        }
        return encomendasFiltradas;
    }

    public List<Encomenda> findePendentes(){
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        for (Encomenda encomenda : todasEncomendas) {
            if (encomenda.getEstado().equals("Em Processamento") || encomenda.getEstado().equals("Por Entregar")) {
                Hibernate.initialize(encomenda.getVolumes());
                encomendasFiltradas.add(encomenda);
            }
        }
        return encomendasFiltradas;
    }

}
