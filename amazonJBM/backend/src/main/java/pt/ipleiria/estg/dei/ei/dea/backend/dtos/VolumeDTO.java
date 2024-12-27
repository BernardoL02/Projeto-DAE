package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {

    private int id;
    private List<ProdutoDTO> produtos = new ArrayList<>();
    private int id_encomenda;

    public VolumeDTO() {

    }

    public VolumeDTO(int id, int id_encomenda) {
        this.id = id;
        this.id_encomenda = id_encomenda;

        if(produtos == null){
            produtos = new ArrayList<>();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public int getId_encomenda() {
        return id_encomenda;
    }

    public void setId_encomenda(int id_encomenda) {
        this.id_encomenda = id_encomenda;
    }

    public static VolumeDTO from(Volume volume) {
        VolumeDTO volumeDTO =  new VolumeDTO(
                volume.getId(),
                volume.getEncomenda().getId()
        );

        List<ProdutoDTO> produtosDTOs = volume.getProdutos().stream().map(ProdutoDTO::from).collect(Collectors.toList());

        volumeDTO.setProdutos(produtosDTOs);

        return volumeDTO;
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
