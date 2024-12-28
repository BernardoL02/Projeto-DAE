package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor")
@NamedQueries({
        @NamedQuery(
                name = "getAllSensores",
                query = "SELECT s FROM Sensor s ORDER BY s.id"
        ),
        @NamedQuery(
                name = "Sensor.findByTipoAndEstado",
                query = "SELECT s FROM Sensor s WHERE s.tipo.id = :tipoId AND s.estado = 'ativo'"
        )
})
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Tipo_Sensores tipo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "bateria")
    private int bateria;

    @Column(name = "Val_Max", nullable = true)
    private Integer valMax;

    @Column(name = "Val_Min", nullable = true)
    private Integer valMin;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "id_embalagem", nullable = false)
    private Embalagem embalagem;

    public Sensor(String valor, Tipo_Sensores tipo, String estado, int bateria, int valMax, int valMin, Embalagem embalagem) {
        this.valor = valor;
        this.tipo = tipo;
        this.estado = estado;
        this.bateria = bateria;
        this.valMax = valMax;
        this.valMin = valMin;
        this.timeStamp = LocalDateTime.now();
        this.embalagem = embalagem;
    }

    public Sensor(String valor, Tipo_Sensores tipo, String estado, int bateria, Embalagem embalagem) {
        this.valor = valor;
        this.tipo = tipo;
        this.estado = estado;
        this.bateria = bateria;
        this.valMax = null;
        this.valMin = null;
        this.timeStamp = LocalDateTime.now();
        this.embalagem = embalagem;
    }

    public Sensor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
        this.timeStamp = LocalDateTime.now();
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
        this.timeStamp = LocalDateTime.now();
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public Integer getValMax() {
        return valMax;
    }

    public void setValMax(int valMax) {
        this.valMax = valMax;
    }

    public Integer getValMin() {
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

    public Embalagem getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(Embalagem embalagem) {
        this.embalagem = embalagem;
    }
}
