package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "sensor"
)
public class Sensor {
    @Id
    private long id;

    @Column(name="valor")
    private int valor;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Tipo_Sensores tipo;

    @Column(name="estado")
    private String estado;

    @Column(name="Val_Max", nullable = true)
    private Integer valMax;

    @Column(name="Val_Min", nullable = true)
    private Integer valMin;

    @Column(name="time_stamp")
    private LocalDateTime timeStamp;

    public Sensor(long id, int valor, Tipo_Sensores tipo, String estado, int valMax, int valMin) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.estado = estado;
        this.valMax = valMax;
        this.valMin = valMin;
        timeStamp = LocalDateTime.now();
    }
    public Sensor(long id, int valor, Tipo_Sensores tipo, String estado) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.valMax = null;
        this.valMin = null;
        this.estado = estado;
        timeStamp = LocalDateTime.now();
    }

    public Sensor() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
        timeStamp = LocalDateTime.now();
    }

    public Tipo_Sensores getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Sensores tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        timeStamp = LocalDateTime.now();
    }

    public int getValMax() {
        return valMax;
    }

    public void setValMax(int valMax) {
        this.valMax = valMax;
    }

    public int getValMin() {
        return valMin;
    }

    public void setValMin(int valMin) {
        this.valMin = valMin;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
