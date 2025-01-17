package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class ProdutoBean {

    @PersistenceContext
    private EntityManager em;

    public Response create(int id, String nome, int categoria_id){

        Produto existingProduto = em.find(Produto.class, id);
        if (existingProduto != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe um Produto com o ID fornecido!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        Categoria categoria = em.find(Categoria.class, categoria_id);

        if(categoria == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Categoria não encontrada!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (em.createNamedQuery("existsProdutoByNomeAndCategoria", Long.class).setParameter("nome", nome).setParameter("categoriaId", categoria_id).getSingleResult() > 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe um produto com o mesmo nome nesta categoria!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        Produto produto = new Produto(id, nome, categoria);

        em.persist(produto);

        return Response.ok("Produto criado com sucesso.").build();
    }

    public List<Produto> findAll() {
        List<Produto> produtos = em.createNamedQuery("getAllProdutos", Produto.class).getResultList();
        return produtos;
    }

    public Produto find(int id) {
        var produto = em.find(Produto.class, id);

        return produto;
    }
}
