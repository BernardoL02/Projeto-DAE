package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;

public class EmbalagemCreateEncomendaDTO implements Serializable {
    private int id;
    private ProdutoCreateEncomendaDTO produto;
    private int quantidade;

    public EmbalagemCreateEncomendaDTO(int id, ProdutoCreateEncomendaDTO produto, int quantidade) {
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

    public ProdutoCreateEncomendaDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoCreateEncomendaDTO produto) {
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
        ProdutoCreateEncomendaDTO produtoCreateEncomendaDTO = new ProdutoCreateEncomendaDTO(
                produto.getId(),
                produto.getQuantidade_ultima_encomenda()
        );

        return new EmbalagemCreateEncomendaDTO(
                embalagem.getId(),
                produtoCreateEncomendaDTO,
                embalagem.getQuantidade()
        );
    }
}
