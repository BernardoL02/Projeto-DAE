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


    public void create(String username, String password, String email, String nome){
        var gestor = new Gestor(username, password, email, nome);
        em.persist(gestor);
    }

}
