package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeCreateEncomendaDTO implements Serializable {

    private int id;
    private List<EmbalagemCreateEncomendaDTO> embalagens = new ArrayList<>();

    public VolumeCreateEncomendaDTO(int id,List<EmbalagemCreateEncomendaDTO> embalagens) {
        this.id=id;
        this.embalagens = embalagens;
    }

    public VolumeCreateEncomendaDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<EmbalagemCreateEncomendaDTO> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<EmbalagemCreateEncomendaDTO> embalagens) {
        this.embalagens = embalagens;
    }

    public static VolumeCreateEncomendaDTO from(Volume volume) {

        return new VolumeCreateEncomendaDTO(volume.getId(),EmbalagemCreateEncomendaDTO.from(volume.getEmbalagens()));

    }

    public static List<VolumeCreateEncomendaDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeCreateEncomendaDTO::from).collect(Collectors.toList());
    }
}