package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmbalagemDTO implements Serializable {
    private int id;
    private ProdutoCreateEncomendaDTO produto;
    private List<SensorDTO> sensores = new ArrayList<>();
    private int quantidade;

    public EmbalagemDTO(int id, ProdutoCreateEncomendaDTO produto, List<SensorDTO> sensores, int quantidade) {
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

    public ProdutoCreateEncomendaDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoCreateEncomendaDTO produto) {
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

        ProdutoCreateEncomendaDTO produto = new ProdutoCreateEncomendaDTO(embalagem.getProduto().getId());

        return new EmbalagemDTO(
                embalagem.getId(),
                produto,
                sensorDTO,
                embalagem.getQuantidade()
        );
    }
}
