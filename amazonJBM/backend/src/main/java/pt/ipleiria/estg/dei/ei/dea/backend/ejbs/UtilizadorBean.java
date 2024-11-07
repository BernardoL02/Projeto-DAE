package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Hasher;

import java.util.NoSuchElementException;

@Stateless
public class UtilizadorBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Utilizador findOrFail(String username) {
        var user = em.getReference(Utilizador.class, username);
        Hibernate.initialize(user);
        return user;
    }
    public boolean canLogin(String username, String password) {
        var user = find(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public Utilizador find(String username) {
        var user = em.find(Utilizador.class, username);
        if (user == null) {
            throw new NoSuchElementException("Utilizador com username " + username + " não encontrado.");
        }
        return user;
    }

}
