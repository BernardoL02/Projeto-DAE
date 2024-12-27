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

    private int quantidade_por_volume;

    @ManyToOne
    @JoinColumn(name = "embalagem_id", nullable = false)
    private Tipo_Embalagem embalagem;

    public Produto(int id, String nome, Categoria categoria, int quantidade_por_volume, Tipo_Embalagem embalagem) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade_por_volume = quantidade_por_volume;
        this.embalagem = embalagem;
    }

    public Produto(){

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade_por_volume() {
        return quantidade_por_volume;
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

    public void setQuantidade_por_volume(int quantidade_por_volume) {
        this.quantidade_por_volume = quantidade_por_volume;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Tipo_Embalagem getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(Tipo_Embalagem embalagem) {
        this.embalagem = embalagem;
    }
}
