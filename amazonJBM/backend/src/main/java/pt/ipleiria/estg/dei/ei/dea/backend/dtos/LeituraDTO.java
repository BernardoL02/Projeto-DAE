package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Leitura;

import java.io.Serializable;

public class LeituraDTO implements Serializable {

    private int id_sensor;
    private int bateria;
    private String valor;

    public LeituraDTO(int id_sensor, int bateria, String valor) {
        this.id_sensor = id_sensor;
        this.bateria = bateria;
        this.valor = valor;
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

    public static LeituraDTO from(Leitura leitura) {
        return new LeituraDTO(
                leitura.getSensor().getId(),
                leitura.getBateria(),
                leitura.getValor()
        );
    }

}
