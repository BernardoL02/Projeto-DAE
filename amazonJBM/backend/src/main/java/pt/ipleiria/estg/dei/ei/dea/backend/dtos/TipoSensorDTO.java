package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class TipoSensorDTO implements Serializable {

    private Integer id;

    public TipoSensorDTO(Integer id) {
        this.id = id;
    }

    public TipoSensorDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static TipoSensorDTO from(Tipo_Sensores tipoSensores) {
        return new TipoSensorDTO(
                tipoSensores.getId()
        );
    }

    public static List<TipoSensorDTO> from(List<Tipo_Sensores> tipoSensores) {
        return tipoSensores.stream().map(TipoSensorDTO::from).collect(Collectors.toList());
    }
}