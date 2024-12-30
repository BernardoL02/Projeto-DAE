package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoCreateEncomendaDTO implements Serializable {

    private int id;

    public ProdutoCreateEncomendaDTO(int id) {
        this.id = id;
    }

    public ProdutoCreateEncomendaDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public static ProdutoCreateEncomendaDTO from(Produto produto) {

        return new ProdutoCreateEncomendaDTO(
                produto.getId()
        );
    }

    public static List<ProdutoCreateEncomendaDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoCreateEncomendaDTO::from).collect(Collectors.toList());
    }
}
