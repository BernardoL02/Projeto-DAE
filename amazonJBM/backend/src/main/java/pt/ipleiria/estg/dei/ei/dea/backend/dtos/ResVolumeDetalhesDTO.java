package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResVolumeDetalhesDTO implements Serializable {
    private int id;
    private String nome_produto;
    private List<ResSensorDetalhesDTO> sensores = new ArrayList<>();
    private int quantidade;


    public ResVolumeDetalhesDTO(int id, String nome_produto, int quantidade) {
        this.id = id;
        this.nome_produto = nome_produto;
        this.quantidade = quantidade;

        if(sensores == null){
            sensores = new ArrayList<>();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public List<ResSensorDetalhesDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<ResSensorDetalhesDTO> sensores) {
        this.sensores = sensores;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public static ResVolumeDetalhesDTO from(Volume volume) {

        List<ResSensorDetalhesDTO> sensorDTOs = volume.getSensores().stream().map(ResSensorDetalhesDTO::from).collect(Collectors.toList());

        ResVolumeDetalhesDTO volumeDTO =  new ResVolumeDetalhesDTO(
                volume.getId(),
                volume.getProduto().getNome(),
                volume.getQuantidade()
        );

        volumeDTO.setSensores(sensorDTOs);

        return volumeDTO;
    }

    public static List<ResVolumeDetalhesDTO> from(List<Volume> volumes) {
        return volumes.stream().map(ResVolumeDetalhesDTO::from).collect(Collectors.toList());
    }
}
