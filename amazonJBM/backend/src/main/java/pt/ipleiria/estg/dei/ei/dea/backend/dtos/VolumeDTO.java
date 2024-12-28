package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {

    private List<ProdutoDTO> produtos = new ArrayList<>();


    public VolumeDTO(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }
    public VolumeDTO() {

    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public static VolumeDTO from(Volume volume) {
        List<ProdutoDTO> produtos = new ArrayList<>();

        for(Embalagem embalagem : volume.getEmbalagens()){
            Produto produto = embalagem.getProduto();
            ProdutoDTO produtoDTO = new ProdutoDTO(
                    produto.getId(),
                    produto.getQuantidade_de_produtos_comprados()
            );
            produtos.add(produtoDTO);
        }

        VolumeDTO volumeDTO =  new VolumeDTO(produtos);

        return volumeDTO;
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }

}
