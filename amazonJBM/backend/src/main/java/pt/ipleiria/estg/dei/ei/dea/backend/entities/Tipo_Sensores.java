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
    private long id;

    private String tipo;

    @OneToMany(mappedBy = "tipo")
    private List<Sensor> sensors;

    public Tipo_Sensores(long id, String tipo) {
        this.id = id;
        this.tipo = tipo;

        if(sensors == null){
            sensors = new ArrayList<>();
        }

    }

    public Tipo_Sensores() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
