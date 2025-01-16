package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProdutos",
                query = "SELECT p FROM Produto p"
        ),
        @NamedQuery(
                name = "existsProdutoByNomeAndCategoria",
                query = "SELECT COUNT(p) FROM Produto p WHERE p.nome = :nome AND p.categoria.id = :categoriaId"
        )
})
public class Produto {

    @Id
    private int id;

    private String nome;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    public Produto(int id, String nome, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Produto(){

    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
