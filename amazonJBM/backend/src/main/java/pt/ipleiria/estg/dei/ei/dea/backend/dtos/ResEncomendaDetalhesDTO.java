package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResEncomendaDetalhesDTO implements Serializable {

    /*
    private int id;

    private String username;

    private String estado;

    private LocalDateTime data_expedicao;

    private LocalDateTime data_entrega;

    private List<ResVolumeDetalhesDTO> volumes = new ArrayList<>();


    public ResEncomendaDetalhesDTO(int id, String username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega) {
        this.id = id;
        this.username = username;
        this.estado = estado;
        this.data_expedicao = data_expedicao;
        this.data_entrega = data_entrega;

        if(volumes == null){
            volumes = new ArrayList<>();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(LocalDateTime data_entrega) {
        this.data_entrega = data_entrega;
    }

    public List<ResVolumeDetalhesDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<ResVolumeDetalhesDTO> volumes) {
        this.volumes = volumes;
    }

    public static ResEncomendaDetalhesDTO from(Encomenda encomenda, String frontEnd) {

        ResEncomendaDetalhesDTO dto = new ResEncomendaDetalhesDTO(
                encomenda.getId(),
                encomenda.getCliente().getUsername(),
                encomenda.getEstado(),
                encomenda.getData_expedicao(),
                encomenda.getData_entrega()
        );

        List<ResVolumeDetalhesDTO> volumeDTOs = encomenda.getVolumes().stream()
                .map(volume -> ResVolumeDetalhesDTO.from(volume, frontEnd)) // Adicionando o parâmetro frontEnd
                .collect(Collectors.toList());

        dto.setVolumes(volumeDTOs);

        return dto;
    }

    public static List<EncomendasDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(EncomendasDTO::from).collect(Collectors.toList());
    }

     */
}
