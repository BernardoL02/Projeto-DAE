package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Leitura;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LeituraDTO implements Serializable {

    private int id_sensor;
    private int bateria;
    private String valor;
    private LocalDateTime timeStamp;

    public LeituraDTO(int id_sensor, int bateria, String valor,LocalDateTime timeStamp) {
        this.id_sensor = id_sensor;
        this.bateria = bateria;
        this.valor = valor;
        this.timeStamp = timeStamp;
    }

    public LeituraDTO(){

    }

    public int getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(int id_sensor) {
        this.id_sensor = id_sensor;
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

    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }
    public void setTimeStamp(LocalDateTime timeStamp){
        this.timeStamp = timeStamp;
    }

    public static LeituraDTO from(Leitura leitura) {
        return new LeituraDTO(
                leitura.getSensor().getId(),
                leitura.getBateria(),
                leitura.getValor(),
                leitura.getTimeStamp()
        );
    }

}
