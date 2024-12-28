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

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private int quantidade_de_produtos_comprados;

    public Produto(int id, String nome, Categoria categoria, int quantidade_de_produtos_comprados) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade_de_produtos_comprados = quantidade_de_produtos_comprados;
    }

    public Produto(){

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade_de_produtos_comprados() {
        return quantidade_de_produtos_comprados;
    }

    public void setQuantidade_de_produtos_comprados(int quantidade_de_produtos_comprados) {
        this.quantidade_de_produtos_comprados = quantidade_de_produtos_comprados;
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
