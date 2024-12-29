package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class TipoEmbalagemBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String tipo) {
        Tipo_Embalagem tipoEmbalagem = new Tipo_Embalagem(id, tipo);
        em.persist(tipoEmbalagem);
    }

    public Tipo_Embalagem find(int id) {
        Tipo_Embalagem tipo = em.find(Tipo_Embalagem.class, id);

        return tipo;
    }

    public List<Tipo_Embalagem> findAll() {
        return em.createNamedQuery("getTipoEmbalagem", Tipo_Embalagem.class).getResultList();
    }

}
