package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@NamedQuery(
        name = "Leitura.findAllBySensor",
        query = "SELECT l FROM Leitura l " +
                "WHERE l.sensor.id = :sensor_id " +
                "ORDER BY l.timeStamp DESC"
)
@Entity
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "bateria")
    private int bateria;

    @Column(name = "valor")
    private String valor;

    public Leitura(Sensor sensor, int bateria, String valor) {
        this.sensor = sensor;
        this.timeStamp = LocalDateTime.now();
        this.bateria = bateria;
        this.valor = valor;
    }

    public Leitura(){

    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
