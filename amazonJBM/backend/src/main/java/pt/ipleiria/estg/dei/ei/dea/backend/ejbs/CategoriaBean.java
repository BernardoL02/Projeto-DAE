package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;

import java.util.NoSuchElementException;

@Stateless
public class CategoriaBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String nome, String tipo_caixa){
        var categoria = new Categoria(id,nome, tipo_caixa);
        em.persist(categoria);
    }

    public Categoria find(int id){

        var categoria = em.find(Categoria.class, id);

        if(categoria == null){
            throw new NoSuchElementException();
        }

        return categoria;
    }


}
