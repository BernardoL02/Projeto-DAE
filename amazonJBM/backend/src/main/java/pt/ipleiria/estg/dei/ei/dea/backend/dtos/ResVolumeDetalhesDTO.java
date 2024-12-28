package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResVolumeDetalhesDTO<T> implements Serializable {

    private int id;
    private Encomenda encomenda;
    private List<Embalagem> embalagems = new ArrayList<>();

    public ResVolumeDetalhesDTO(int id, Encomenda encomenda, List<Embalagem> embalagems) {
        this.id = id;
        this.encomenda = encomenda;
        this.embalagems = embalagems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public List<Embalagem> getEmbalagems() {
        return embalagems;
    }

    public void setEmbalagems(List<Embalagem> embalagems) {
        this.embalagems = embalagems;
    }


    public static ResVolumeDetalhesDTO from(Volume volume, String frontEnd) {

        ResVolumeDetalhesDTO volumeDTO =  new ResVolumeDetalhesDTO(
                volume.getId(),
                volume.getEncomenda(),
                volume.getEncomenda().getVolumes()
        );

        return volumeDTO;
    }

    public static List<ResVolumeDetalhesDTO> from(List<Volume> volumes, String frontEnd) {
        return volumes.stream().map(volume -> from(volume, frontEnd)).collect(Collectors.toList());
    }


}
