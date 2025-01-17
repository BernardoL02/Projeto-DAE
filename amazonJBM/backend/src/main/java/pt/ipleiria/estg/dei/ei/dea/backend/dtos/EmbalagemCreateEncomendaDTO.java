package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class EmbalagemCreateEncomendaDTO implements Serializable {
    private int id;
    private int id_tipoEmbalagem;
    private ProdutoCreateEncomendaDTO produto;
    private int quantidade;

    public EmbalagemCreateEncomendaDTO(int id,int id_tipoEmbalagem, ProdutoCreateEncomendaDTO produto, int quantidade) {
        this.id = id;
        this.id_tipoEmbalagem = id_tipoEmbalagem;
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

    public int getTipo() {
        return id_tipoEmbalagem;
    }

    public void setTipo(int id) {
        this.id_tipoEmbalagem = id;
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

        return new EmbalagemCreateEncomendaDTO(
                embalagem.getId(),
                embalagem.getTipo().getId(),
                ProdutoCreateEncomendaDTO.from(embalagem.getProduto()),
                embalagem.getQuantidade()
        );
    }

    public static List<EmbalagemCreateEncomendaDTO> from(List<Embalagem> embalagens) {
        return embalagens.stream().map(EmbalagemCreateEncomendaDTO::from).collect(Collectors.toList());
    }

}
