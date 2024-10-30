package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {

    private int id_produto;
    private String nome_produto;
    private int id_encomenda;
    private List<SensorDTO> sensores = new ArrayList<>();
    private int quantidade;


    public VolumeDTO(int id_produto,String nome_produto, int id_encomenda, int quantidade) {
        this.id_produto = id_produto;
        this.nome_produto = nome_produto;
        this.id_encomenda = id_encomenda;
        this.quantidade = quantidade;

        if(sensores == null){
            sensores = new ArrayList<>();
        }
    }
    public VolumeDTO() {

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
        return new VolumeDTO(
                volume.getProduto().getId(),
                volume.getProduto().getNome(),
                volume.getEncomenda().getId(),
                volume.getQuantidade()
        );
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
