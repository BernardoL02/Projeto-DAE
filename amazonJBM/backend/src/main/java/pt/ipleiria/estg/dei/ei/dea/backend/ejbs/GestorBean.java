package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Logista;


@Stateless
public class GestorBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome){
        var gestor = new Gestor(username, password, email, nome);
        em.persist(gestor);
    }

    public void cancelarEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id); //Todo -> Ver se encomenda existe e ver se esta em processamento se nao nao e possivel atualizar

        encomenda.setEstado("Cancelada");
        em.merge(encomenda);
    }

}
