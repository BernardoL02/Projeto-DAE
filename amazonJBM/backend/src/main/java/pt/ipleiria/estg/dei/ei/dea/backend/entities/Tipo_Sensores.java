package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "tipo_sensores"
)
@NamedQueries({
        @NamedQuery(
                name = "getTipoSensor",
                query = "SELECT t FROM Tipo_Sensores t "
        ),
        @NamedQuery(
                name = "existsTipoSensor",
                query = "SELECT COUNT(s) FROM Tipo_Sensores s WHERE s.tipo = :tipo"
        )
})
public class Tipo_Sensores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tipo;

    @ManyToMany(mappedBy = "tipoSensores")
    private List<Tipo_Embalagem> embalagens = new ArrayList<>();

    public Tipo_Sensores(String tipo) {
        this.tipo = tipo;
    }

    public Tipo_Sensores() {

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

}
