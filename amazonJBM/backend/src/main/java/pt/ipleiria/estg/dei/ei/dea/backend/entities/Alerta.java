package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(
                name = "getAlertasByEncomendaId",
                query = "SELECT a FROM Alerta a " +
                        "JOIN Volume v ON v.id = a.volume.id " +
                        "JOIN Encomenda e ON e.id = v.encomenda.id WHERE e.id = :encomendaId"
        ),
        @NamedQuery(
                name = "Alerta.findAll",
                query = "SELECT a FROM Alerta a ORDER BY a.time_stamp DESC"
        ),
        @NamedQuery(
                name = "Alerta.findAllByCliente",
                query = "SELECT a FROM Alerta a " +
                        "WHERE a.sensor.id = :sensor_id " +
                        "ORDER BY a.time_stamp DESC"
        )
})

@Entity
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    private String valor;

    private int bateria;

    private LocalDateTime time_stamp;

    @ManyToOne
    @JoinColumn(name = "id_volume", nullable = false)
    private Volume volume;


    public Alerta(String mensagem, Sensor sensor, String valor, int bateria, Volume volume) {
        this.mensagem = mensagem;
        this.sensor = sensor;
        this.valor = valor;
        this.volume = volume;
        this.bateria = bateria;
        this.time_stamp = LocalDateTime.now();
    }

    public Alerta(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public LocalDateTime getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(LocalDateTime time_stamp) {
        this.time_stamp = time_stamp;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }
}
