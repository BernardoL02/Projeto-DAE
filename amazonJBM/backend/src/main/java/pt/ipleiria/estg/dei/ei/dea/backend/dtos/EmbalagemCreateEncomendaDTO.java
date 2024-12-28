package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;

public class EmbalagemCreateEncomendaDTO implements Serializable {
    private int id;
    private ProdutoDTO produto;
    private int quantidade;

    public EmbalagemCreateEncomendaDTO(int id, ProdutoDTO produto, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public EmbalagemCreateEncomendaDTO() {}

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

    public static EmbalagemCreateEncomendaDTO from(Embalagem embalagem) {
        Produto produto = embalagem.getProduto();
        ProdutoDTO produtoDTO = new ProdutoDTO(
                produto.getId(),
                produto.getQuantidade_de_produtos_comprados()
        );

        return new EmbalagemCreateEncomendaDTO(
                embalagem.getId(),
                produtoDTO,
                embalagem.getQuantidade()
        );
    }
}
