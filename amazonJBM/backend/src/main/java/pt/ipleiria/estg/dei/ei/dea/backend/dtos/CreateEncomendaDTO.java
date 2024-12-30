package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateEncomendaDTO implements Serializable {

    private String username;
    private LocalDateTime data_expedicao;
    @JsonProperty("volumes")
    private List<VolumeCreateEncomendaDTO> volumes = new ArrayList<>();

    public CreateEncomendaDTO( String username, LocalDateTime data_expedicao, List<VolumeCreateEncomendaDTO> volumes) {
        this.username = username;
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

    public LocalDateTime getData_expedicao() {
        return data_expedicao;
    }

    public void setData_expedicao(LocalDateTime data_expedicao) {
        this.data_expedicao = data_expedicao;
    }


    public List<VolumeCreateEncomendaDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeCreateEncomendaDTO> volumes) {
        this.volumes = volumes;
    }

    public static CreateEncomendaDTO from(Encomenda encomenda) {

        return new CreateEncomendaDTO(
                encomenda.getCliente().getUsername(),
                encomenda.getData_expedicao(),
                VolumeCreateEncomendaDTO.from(encomenda.getVolumes())

        );
    }

    public static List<CreateEncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(CreateEncomendaDTO::from).collect(Collectors.toList());
    }


}
