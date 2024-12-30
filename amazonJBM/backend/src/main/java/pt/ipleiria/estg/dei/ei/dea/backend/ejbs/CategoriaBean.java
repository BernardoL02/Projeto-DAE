package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.NoSuchElementException;

@Stateless
public class CategoriaBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private TipoEmbalagemBean tipoEmbalagemBean;

    public void create(int id, String nome){
        Categoria categoria = new Categoria(id,nome);
        em.persist(categoria);
    }

    public Categoria find(int id){

        return em.find(Categoria.class, id);
    }


}
