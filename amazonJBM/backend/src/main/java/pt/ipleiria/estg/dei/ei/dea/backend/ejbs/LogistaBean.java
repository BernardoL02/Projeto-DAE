package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Logista;


import java.util.List;

@Stateless
public class LogistaBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome){
        var logista = new Logista(username, password, email, nome);
        em.persist(logista);
    }

}
