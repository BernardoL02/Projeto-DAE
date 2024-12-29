package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProdutos",
                query = "SELECT p FROM Produto p"
        ),
})
public class Produto {

    @Id
    private int id;

    private String nome;

    private int quantidade_ultima_encomenda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    public Produto(int id, String nome, int quantidade_ultima_encomenda, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.quantidade_ultima_encomenda = quantidade_ultima_encomenda;
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

    public int getQuantidade_ultima_encomenda() {
        return quantidade_ultima_encomenda;
    }

    public void setQuantidade_ultima_encomenda(int quantidade_ultima_encomenda) {
        this.quantidade_ultima_encomenda = quantidade_ultima_encomenda;
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
