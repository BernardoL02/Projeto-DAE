package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResTipoSensoresDTO implements Serializable {

    private int id;

    private String tipo;

    public ResTipoSensoresDTO(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public static ResTipoSensoresDTO from(Tipo_Sensores tipoSensores) {
        return new ResTipoSensoresDTO(
                tipoSensores.getId(),
                tipoSensores.getTipo()
        );
    }


    public static List<ResTipoSensoresDTO> from(List<Tipo_Sensores> tipoSensores) {
        return tipoSensores.stream().map(ResTipoSensoresDTO::from).collect(Collectors.toList());
    }
}
