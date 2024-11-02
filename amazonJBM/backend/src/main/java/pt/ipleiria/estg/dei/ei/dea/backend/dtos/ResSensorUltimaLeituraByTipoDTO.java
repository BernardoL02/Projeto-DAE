package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ResSensorUltimaLeituraByTipoDTO implements Serializable {

    private int id;
    private String valor;
    private String tipoNome;
    private String estado;
    private int bateria;
    private LocalDateTime timeStamp;

    public ResSensorUltimaLeituraByTipoDTO(int id, String valor, String tipoNome, String estado, int bateria, LocalDateTime timeStamp) {
        this.id = id;
        this.valor = valor;
        this.tipoNome = tipoNome;
        this.estado = estado;
        this.bateria = bateria;
        this.timeStamp = timeStamp;
    }

    public ResSensorUltimaLeituraByTipoDTO(){

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
    }

    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static ResSensorUltimaLeituraByTipoDTO from(Sensor sensor) {
        return new ResSensorUltimaLeituraByTipoDTO(
                sensor.getId(),
                sensor.getValor(),
                sensor.getTipo().getTipo(),
                sensor.getEstado(),
                sensor.getBateria(),
                sensor.getTimeStamp()
        );
    }

    public static List<ResSensorUltimaLeituraByTipoDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(ResSensorUltimaLeituraByTipoDTO::from).collect(Collectors.toList());
    }
}
