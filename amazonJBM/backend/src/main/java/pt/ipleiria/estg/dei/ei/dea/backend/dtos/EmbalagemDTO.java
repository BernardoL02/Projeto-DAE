package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmbalagemDTO implements Serializable {
    private int id;
    private ProdutoDTO produto;
    private List<SensorDTO> sensores = new ArrayList<>();
    private int quantidade;

    public EmbalagemDTO(int id,ProdutoDTO produto, List<SensorDTO> sensores, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.sensores = sensores;
        this.quantidade = quantidade;
    }

    public EmbalagemDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public static EmbalagemDTO from(Embalagem embalagem) {

        List<SensorDTO> sensorDTO = embalagem.getSensores().stream().map(SensorDTO::from).collect(Collectors.toList());

        return new EmbalagemDTO(
                embalagem.getId(),
                ProdutoDTO.from(embalagem.getProduto()),
                sensorDTO,
                embalagem.getQuantidade()
        );
    }
}
