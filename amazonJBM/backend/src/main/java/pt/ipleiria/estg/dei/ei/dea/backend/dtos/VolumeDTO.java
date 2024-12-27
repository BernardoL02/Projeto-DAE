package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {

    /*private int id;
    private int id_produto;
    private List<ProdutoDTO> produtos = new ArrayList<>();
    private int id_encomenda;
    private List<SensorDTO> sensores = new ArrayList<>();
    private int quantidade;


    public VolumeDTO(int id, int id_produto,String nome_produto, int id_encomenda, int quantidade) {
        this.id = id;
        this.id_produto = id_produto;
        this.id_encomenda = id_encomenda;
        this.quantidade = quantidade;

        if(sensores == null){
            sensores = new ArrayList<>();
        }
    }
    public VolumeDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getId_encomenda() {
        return id_encomenda;
    }

    public void setId_encomenda(int id_encomenda) {
        this.id_encomenda = id_encomenda;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public static VolumeDTO from(Volume volume) {

        List<SensorDTO> sensorDTOs = volume.getSensores().stream().map(SensorDTO::from).collect(Collectors.toList());

        VolumeDTO volumeDTO =  new VolumeDTO(
                volume.getId(),
                volume.getProdutos().getId(),
                volume.getProdutos().getNome(),
                volume.getEncomenda().getId(),
                volume.getQuantidade()
        );

        volumeDTO.setSensores(sensorDTOs);

        return volumeDTO;
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }*/
}
