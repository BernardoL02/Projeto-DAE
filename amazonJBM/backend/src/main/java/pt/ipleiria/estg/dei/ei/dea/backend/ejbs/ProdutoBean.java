package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.NoSuchElementException;

@Stateless
public class ProdutoBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String nome, int categoria_id, int quantidade_por_volume){

        var categoria = em.find(Categoria.class, categoria_id);

        var produto = new Produto(id, nome, categoria, quantidade_por_volume);
        em.persist(produto);
    }

    public Produto find(int id) {
        var produto = em.find(Produto.class, id);
        if (produto == null) {
            throw new NoSuchElementException("Produto com ID " + id + " não encontrado.");
        }
        return produto;
    }
}
