package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class ProdutoBean {

    @PersistenceContext
    private EntityManager em;

    public void create(int id, String nome, int categoria_id, int quantidade_por_volume, int embalagem_id){

        var categoria = em.find(Categoria.class, categoria_id);
        var embalagem = em.find(Tipo_Embalagem.class, embalagem_id);
        var produto = new Produto(id, nome, categoria, quantidade_por_volume, embalagem);
        em.persist(produto);
    }

    public List<Produto> findAll() {
        List<Produto> produtos = em.createNamedQuery("getAllProdutos", Produto.class).getResultList();
        return produtos;
    }

    public Produto find(int id) {
        var produto = em.find(Produto.class, id);
        if (produto == null) {
            throw new NoSuchElementException("Produto com ID " + id + " não encontrado.");
        }
        return produto;
    }
}
