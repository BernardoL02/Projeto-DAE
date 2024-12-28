package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateEncomendaDTO implements Serializable {

    private String username;
    private String estado;
    private LocalDateTime data_expedicao;
    @JsonProperty("volumes")
    private List<VolumeDTO> volumes = new ArrayList<>();

    public CreateEncomendaDTO( String username, String estado, LocalDateTime data_expedicao, List<VolumeDTO> volumes) {
        this.username = username;
        this.estado = estado;
        this.data_expedicao = data_expedicao;
        this.volumes = volumes;
    }

    public CreateEncomendaDTO() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getData_expedicao() {
        return data_expedicao;
    }

    public void setData_expedicao(LocalDateTime data_expedicao) {
        this.data_expedicao = data_expedicao;
    }


    public List<VolumeDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeDTO> volumes) {
        this.volumes = volumes;
    }

    public static CreateEncomendaDTO from(Encomenda encomenda) {
        List<VolumeDTO> volumesDTO = encomenda.getVolumes().stream()
                .map(volume -> {
                    List<ProdutoDTO> produtosDTO = volume.getEmbalagens().stream()
                            .map(produto -> new ProdutoDTO(
                                    produto.getId(),
                                    produto.getQuantidade()
                            ))
                            .collect(Collectors.toList());

                    return new VolumeDTO(produtosDTO);
                })
                .collect(Collectors.toList());

        return new CreateEncomendaDTO(
                encomenda.getCliente().getUsername(),
                encomenda.getEstado(),
                encomenda.getData_expedicao(),
                volumesDTO
        );
    }

    public static List<CreateEncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(CreateEncomendaDTO::from).collect(Collectors.toList());
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Data de Expedição: ").append(data_expedicao).append("\n");
        sb.append("Volumes: ").append(volumes.size()).append("\n");

        // Retorna a string concatenada
        return sb.toString();
    }


}
