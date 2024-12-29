package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResVolumeDetalhesDTO<T> implements Serializable {

    private int id;
    private List<EmbalagemDTO> embalagems = new ArrayList<>();

    public ResVolumeDetalhesDTO(int id, List<EmbalagemDTO> embalagems) {
        this.id = id;
        this.embalagems = embalagems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<EmbalagemDTO> getEmbalagems() {
        return embalagems;
    }

    public void setEmbalagems(List<EmbalagemDTO> embalagems) {
        this.embalagems = embalagems;
    }

    public static ResVolumeDetalhesDTO from(Volume volume) {

        List<EmbalagemDTO> embalagemDTO = volume.getEmbalagens()
                .stream()
                .map(EmbalagemDTO::from)
                .collect(Collectors.toList());

        ResVolumeDetalhesDTO volumeDTO =  new ResVolumeDetalhesDTO(
                volume.getId(),
                embalagemDTO
        );

        return volumeDTO;
    }

    public static List<ResVolumeDetalhesDTO> from(List<Volume> volumes) {
        return volumes.stream().map(volume -> from(volume)).collect(Collectors.toList());
    }


}
