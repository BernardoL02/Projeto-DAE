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
})
public class Tipo_Sensores {
    @Id
    private int id;

    private String tipo;

    public Tipo_Sensores(int id, String tipo) {
        this.id = id;
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
