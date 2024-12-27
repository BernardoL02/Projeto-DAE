package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity

public class Volume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "volume_produto",
            joinColumns = @JoinColumn(
                    name = "volume_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "produto_id",
                    referencedColumnName = "id"
            )
    )
    private List<Produto> produtos = new ArrayList<>();

    @ManyToOne
    @NotNull
    private  Encomenda encomenda;

    public Volume( Encomenda encomenda) {
        if(produtos == null){
            this.produtos = new ArrayList<>();
        }

        this.encomenda = encomenda;
    }

    public Volume(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProduto(List<Produto> produtos) {
        this.produtos = produtos;
    }
    public void addProduto(Produto produto) {
        produtos.add(produto);

    }

    public @NotNull Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(@NotNull Encomenda encomenda) {
        this.encomenda = encomenda;
    }


}
