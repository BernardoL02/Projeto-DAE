package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

@Entity
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    public Leitura(int id, Sensor id_sensor) {
        this.id = id;
        this.sensor = id_sensor;
    }

    public Leitura() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sensor getId_sensor() {
        return sensor;
    }

    public void setId_sensor(Sensor id_sensor) {
        this.sensor = id_sensor;
    }
}
