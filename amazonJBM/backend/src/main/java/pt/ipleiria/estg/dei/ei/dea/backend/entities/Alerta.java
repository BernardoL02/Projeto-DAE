package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(
                name = "getAlertasByEncomendaId",
                query = "SELECT a FROM Alerta a JOIN a.volume v JOIN v.encomenda e WHERE e.id = :encomendaId"
        )
})
@Entity
public class Alerta {

    @Id
    private int id;

    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    private String valor;

    private LocalDateTime time_stamp;

    @ManyToOne
    @JoinColumn(name = "id_volume", nullable = false)
    private Volume volume;


    public Alerta(int id, String mensagem, Sensor sensor, String valor, Volume volume) {
        this.id = id;
        this.mensagem = mensagem;
        this.sensor = sensor;
        this.valor = valor;
        this.volume = volume;
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
