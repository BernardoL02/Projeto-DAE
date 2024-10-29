package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.time.LocalDateTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EncomendaBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClienteBean clienteBean;

    public void create(long id, String client_username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega) {

        Cliente cliente = clienteBean.find(client_username);

        Encomenda encomenda = new Encomenda(id,cliente,estado,data_expedicao,data_entrega);
        em.persist(encomenda);
    }
}
