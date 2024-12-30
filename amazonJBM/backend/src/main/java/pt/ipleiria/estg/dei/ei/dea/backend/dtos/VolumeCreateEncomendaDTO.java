package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeCreateEncomendaDTO implements Serializable {

    private List<EmbalagemCreateEncomendaDTO> embalagens = new ArrayList<>();

    public VolumeCreateEncomendaDTO(List<EmbalagemCreateEncomendaDTO> embalagens) {
        this.embalagens = embalagens;
    }

    public VolumeCreateEncomendaDTO() {

    }

    public List<EmbalagemCreateEncomendaDTO> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<EmbalagemCreateEncomendaDTO> embalagens) {
        this.embalagens = embalagens;
    }

    public static VolumeCreateEncomendaDTO from(Volume volume) {

        return new VolumeCreateEncomendaDTO(EmbalagemCreateEncomendaDTO.from(volume.getEmbalagens()));

    }

    public static List<VolumeCreateEncomendaDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeCreateEncomendaDTO::from).collect(Collectors.toList());
    }
}