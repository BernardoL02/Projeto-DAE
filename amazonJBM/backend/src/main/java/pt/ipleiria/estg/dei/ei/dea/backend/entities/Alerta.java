package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Alerta {

    @Id
    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    private LocalDateTime time_stamp;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Utilizador utilizador;

    @ManyToOne
    @JoinColumn(name = "id_encomenda", nullable = false)
    private Encomenda encomenda;


    public Alerta(String mensagem, Sensor sensor, Utilizador utilizador, Encomenda encomenda) {
        this.mensagem = mensagem;
        this.sensor = sensor;
        this.utilizador = utilizador;
        this.encomenda = encomenda;
        this.time_stamp = LocalDateTime.now();
    }

    public Alerta(){

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

    public LocalDateTime getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(LocalDateTime time_stamp) {
        this.time_stamp = time_stamp;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }
}
