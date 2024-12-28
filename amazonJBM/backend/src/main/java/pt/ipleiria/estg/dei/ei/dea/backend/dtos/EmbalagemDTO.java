package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;

public class EmbalagemDTO implements Serializable {
    private int id;
    private ProdutoDTO produto;
    private int quantidade;

    public EmbalagemDTO(int id, ProdutoDTO produto, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public EmbalagemDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // Método de mapeamento
    public static EmbalagemDTO from(Embalagem embalagem) {
        Produto produto = embalagem.getProduto();
        ProdutoDTO produtoDTO = new ProdutoDTO(
                produto.getId(),
                produto.getQuantidade_de_produtos_comprados() // Adapte para os atributos do Produto
        );

        return new EmbalagemDTO(
                embalagem.getId(),
                produtoDTO,
                embalagem.getQuantidade()
        );
    }
}
