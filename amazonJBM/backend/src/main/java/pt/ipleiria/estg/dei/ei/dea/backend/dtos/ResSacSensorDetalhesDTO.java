package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ResSacSensorDetalhesDTO implements Serializable {

    private String valor;
    private String tipoNome;
    private LocalDateTime timeStamp;

    public ResSacSensorDetalhesDTO(String valor, String tipoNome, LocalDateTime timeStamp) {
        this.valor = valor;
        this.tipoNome = tipoNome;
        this.timeStamp = timeStamp;
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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static ResSacSensorDetalhesDTO from(Sensor sensor) {
        return new ResSacSensorDetalhesDTO(
                sensor.getValor(),
                sensor.getTipo().getTipo(),
                sensor.getTimeStamp()
        );
    }

    public static List<ResSacSensorDetalhesDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(ResSacSensorDetalhesDTO::from).collect(Collectors.toList());
    }

}
