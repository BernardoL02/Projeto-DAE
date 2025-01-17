package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTipoSensorDTO implements Serializable {

    private int id;

    private String tipo;

    public CreateTipoSensorDTO(int id, String tipo) {
        this.tipo = tipo;
    }

    public CreateTipoSensorDTO() {

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

    public static CreateTipoSensorDTO from(Tipo_Sensores tipoSensores) {
        return new CreateTipoSensorDTO(
                tipoSensores.getId(),
               tipoSensores.getTipo()
        );
    }
}
