package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class EmbalagemBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id) {
        Tipo_Embalagem tipoEmbalagem = em.find(Tipo_Embalagem.class,id);
        Embalagem embalagem = new Embalagem(tipoEmbalagem);
        em.persist(embalagem);
    }

    public Embalagem find(int id) {
        var embalagem = em.find(Embalagem.class, id);
        if (embalagem == null) {
            throw new NoSuchElementException("Embalagem com ID " + id + " não encontrado.");
        }
        return embalagem;
    }

}
