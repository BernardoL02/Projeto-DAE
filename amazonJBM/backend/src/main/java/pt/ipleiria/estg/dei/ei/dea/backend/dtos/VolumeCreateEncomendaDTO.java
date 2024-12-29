package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VolumeCreateEncomendaDTO implements Serializable {

    private List<ProdutoCreateEncomendaDTO> produtos = new ArrayList<>();


    public VolumeCreateEncomendaDTO(List<ProdutoCreateEncomendaDTO> produtos) {
        this.produtos = produtos;
    }

    public VolumeCreateEncomendaDTO() {

    }

    public List<ProdutoCreateEncomendaDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoCreateEncomendaDTO> produtos) {
        this.produtos = produtos;
    }

    public static VolumeCreateEncomendaDTO from(Volume volume) {
        List<ProdutoCreateEncomendaDTO> produtos = new ArrayList<>();

        for (Embalagem embalagem : volume.getEmbalagens()) {
            Produto produto = embalagem.getProduto();
            ProdutoCreateEncomendaDTO produtoCreateEncomendaDTO = new ProdutoCreateEncomendaDTO(
                    produto.getId(),
                    embalagem.getQuantidade()
            );
            produtos.add(produtoCreateEncomendaDTO);
        }

        VolumeCreateEncomendaDTO volumeCreateEncomendaDTO = new VolumeCreateEncomendaDTO(produtos);

        return volumeCreateEncomendaDTO;
    }
}