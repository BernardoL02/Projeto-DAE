package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResTipoSensorDTO implements Serializable {

    private String tipo;

    public ResTipoSensorDTO(String tipo) {
        this.tipo = tipo;
    }
    public ResTipoSensorDTO() {

    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public static ResTipoSensorDTO from(Tipo_Sensores tipoSensores) {
        return new ResTipoSensorDTO(
                tipoSensores.getTipo()
        );
    }

    public static List<ResTipoSensorDTO> from(List<Tipo_Sensores> tipoSensores) {
        return tipoSensores.stream().map(ResTipoSensorDTO::from).collect(Collectors.toList());
    }
}