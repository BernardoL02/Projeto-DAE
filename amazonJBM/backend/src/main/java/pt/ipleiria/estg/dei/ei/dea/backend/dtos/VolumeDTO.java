package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {

    private int id;
    private int encomendaId;
    private List<EmbalagemDTO> embalagens = new ArrayList<>();

    public VolumeDTO(int id, int encomendaId, List<EmbalagemDTO> embalagens) {
        this.id = id;
        this.encomendaId = encomendaId;
        this.embalagens = embalagens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(int encomendaId) {
        this.encomendaId = encomendaId;
    }

    public List<EmbalagemDTO> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<EmbalagemDTO> embalagens) {
        this.embalagens = embalagens;
    }

    public static VolumeDTO from(Volume volume) {

        List<EmbalagemDTO> embalagemCreateEncomendaDTO = volume.getEmbalagens().stream().map(EmbalagemDTO::from).collect(Collectors.toList());

        VolumeDTO dto = new VolumeDTO(
                volume.getId(),
                volume.getEncomenda().getId(),
                embalagemCreateEncomendaDTO
        );

        return dto;
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
