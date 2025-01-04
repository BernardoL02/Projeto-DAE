package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Tipo_Sensores;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResTipoEmbalagemDTO implements Serializable {

    private int id;
    private String tipo;

    public ResTipoEmbalagemDTO(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public ResTipoEmbalagemDTO() {

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

    public static ResTipoEmbalagemDTO from(Tipo_Embalagem tipoEmbalagem) {
        return new ResTipoEmbalagemDTO(
                tipoEmbalagem.getId(),
                tipoEmbalagem.getTipo()
        );
    }

    public static List<ResTipoEmbalagemDTO> from(List<Tipo_Embalagem> tipoEmbalagens) {
        return tipoEmbalagens.stream().map(ResTipoEmbalagemDTO::from).collect(Collectors.toList());
    }
}
