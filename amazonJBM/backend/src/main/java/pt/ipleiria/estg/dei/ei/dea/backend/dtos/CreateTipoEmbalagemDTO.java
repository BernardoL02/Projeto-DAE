package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;

public class CreateTipoEmbalagemDTO implements Serializable {

    private int id;

    private String tipo;

    private List<TipoSensorDTO> tipos_sensores;

    public CreateTipoEmbalagemDTO(int id, String tipo, List<TipoSensorDTO> tipos_sensores) {
        this.id = id;
        this.tipo = tipo;
        this.tipos_sensores = tipos_sensores;
    }

    public CreateTipoEmbalagemDTO() {

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

    public List<TipoSensorDTO> getTipos_sensores() {
        return tipos_sensores;
    }

    public void setTipos_sensores(List<TipoSensorDTO> tipos_sensores) {
        this.tipos_sensores = tipos_sensores;
    }

    public static CreateTipoEmbalagemDTO from(Tipo_Embalagem tipoEmbalagem) {
        return new CreateTipoEmbalagemDTO(
                tipoEmbalagem.getId(),
                tipoEmbalagem.getTipo(),
                TipoSensorDTO.from(tipoEmbalagem.getSensores())
        );
    }
}
