package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResVolumeDetalhesDTO<T> implements Serializable { // Classe parametrizada com T
    private int id;
    private List<T> produtos = new ArrayList<>(); // Lista tipada com T
    private Encomenda encomenda;

    public ResVolumeDetalhesDTO(int id, Encomenda encomenda) {
        this.id = id;
        this.encomenda = encomenda;

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

    public List<T> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<T> produtos) {
        this.produtos = produtos;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public static ResVolumeDetalhesDTO from(Volume volume, String frontEnd) {

        List<ProdutoDTO> produtosDTOs = volume.getProdutos().stream().map(ProdutoDTO::from).collect(Collectors.toList());

        ResVolumeDetalhesDTO volumeDTO =  new ResVolumeDetalhesDTO(
                volume.getId(),
                volume.getEncomenda()
        );

        volumeDTO.setProdutos(produtosDTOs);

        return volumeDTO;
    }

    public static List<ResVolumeDetalhesDTO> from(List<Volume> volumes, String frontEnd) {
        return volumes.stream().map(volume -> from(volume, frontEnd)).collect(Collectors.toList());
    }
}
