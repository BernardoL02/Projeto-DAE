package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Logista;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Hasher;


import java.util.ArrayList;
import java.util.List;

@Stateless
public class LogistaBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome){
        var logista = new Logista(username, Hasher.hash(password), email, nome);
        em.persist(logista);
    }


}
