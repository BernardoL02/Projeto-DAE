package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmbalagemDTO implements Serializable {
    private int id;
    private ResProdutoDTO produto;
    private List<SensorDTO> sensores = new ArrayList<>();
    private int quantidade;
    private String tipoEmbalagem;

    public EmbalagemDTO(int id, ResProdutoDTO produto, List<SensorDTO> sensores, int quantidade, String tipoEmbalagem) {
        this.id = id;
        this.produto = produto;
        this.sensores = sensores;
        this.quantidade = quantidade;
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public EmbalagemDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ResProdutoDTO produto) {
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

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public static EmbalagemDTO from(Embalagem embalagem) {

        List<SensorDTO> sensorDTO = embalagem.getSensores().stream().map(SensorDTO::from).collect(Collectors.toList());

        ResProdutoDTO produto = new ResProdutoDTO(embalagem.getProduto().getId(), embalagem.getProduto().getNome());

        return new EmbalagemDTO(
                embalagem.getId(),
                produto,
                sensorDTO,
                embalagem.getQuantidade(),
                embalagem.getTipo().getTipo()
        );
    }
}
