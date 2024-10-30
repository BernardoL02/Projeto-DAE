package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
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
        return encomenda;
    }

    public List<Encomenda> findAllEncomendasEmProcessamento() {
        return em.createNamedQuery("getAllEncomendasEmProcessamento", Encomenda.class).getResultList();
    }

}
