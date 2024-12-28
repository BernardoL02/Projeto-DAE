package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO implements Serializable {

    private int id;
    private int quantidade_de_produtos_comprados;

    public ProdutoDTO(int id,  int quantidade_de_produtos_comprados) {
        this.id = id;
        this.quantidade_de_produtos_comprados = quantidade_de_produtos_comprados;
    }

    public ProdutoDTO(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade_de_produtos_comprados() {
        return quantidade_de_produtos_comprados;
    }

    public void setQuantidade_de_produtos_comprados(int quantidade_de_produtos_comprados) {
        this.quantidade_de_produtos_comprados = quantidade_de_produtos_comprados;
    }

    public static ProdutoDTO from(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getQuantidade_de_produtos_comprados()
        );
    }

    public static List<ProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTO::from).collect(Collectors.toList());
    }
}
