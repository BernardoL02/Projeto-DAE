package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResVolumeDetalhesDTO<T> implements Serializable { // Classe parametrizada com T
    private int id;
    private String nome_produto;
    private List<T> sensores = new ArrayList<>(); // Lista tipada com T
    private int quantidade;

    public ResVolumeDetalhesDTO(int id, String nome_produto, int quantidade) {
        this.id = id;
        this.nome_produto = nome_produto;
        this.quantidade = quantidade;
    }

    // Getters e setters
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

    public List<T> getSensores() {
        return sensores;
    }

    public void setSensores(List<T> sensores) {
        this.sensores = sensores;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public static ResVolumeDetalhesDTO from(Volume volume, String frontEnd) {

        ResVolumeDetalhesDTO volumeDTO =  new ResVolumeDetalhesDTO(
                volume.getId(),
                volume.getProduto().getNome(),
                volume.getQuantidade()
        );

        if(frontEnd.equalsIgnoreCase("SAC")){
            List<ResSacSensorDetalhesDTO> sensorDTOs = volume.getSensores().stream().map(ResSacSensorDetalhesDTO::from).collect(Collectors.toList());
            volumeDTO.setSensores(sensorDTOs);
        }else{
            List<ResSensorDetalhesDTO> sensorDTOs = volume.getSensores().stream().map(ResSensorDetalhesDTO::from).collect(Collectors.toList());
            volumeDTO.setSensores(sensorDTOs);
        }

        return volumeDTO;
    }

    public static List<ResVolumeDetalhesDTO> from(List<Volume> volumes, String frontEnd) {
        return volumes.stream().map(volume -> from(volume, frontEnd)).collect(Collectors.toList());
    }
}
