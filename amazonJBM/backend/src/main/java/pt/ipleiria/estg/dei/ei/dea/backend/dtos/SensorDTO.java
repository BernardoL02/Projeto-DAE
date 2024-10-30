package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO implements Serializable {
    private int id;
    private int valor;
    private int tipoId;
    private String estado;
    private Integer valMax;
    private Integer valMin;
    private LocalDateTime timeStamp;
    private int volumeId;

    public SensorDTO() {}

    public SensorDTO(int id, int valor, int tipoId, String estado, Integer valMax, Integer valMin, int volumeId) {
        this.id = id;
        this.valor = valor;
        this.tipoId = tipoId;
        this.estado = estado;
        this.valMax = valMax;
        this.valMin = valMin;
        this.timeStamp = LocalDateTime.now();
        this.volumeId = volumeId;
    }

    public SensorDTO(int id, int valor, int tipoId, String estado, int volumeId) {
        this.id = id;
        this.valor = valor;
        this.tipoId = tipoId;
        this.estado = estado;
        this.valMax = null;
        this.valMin = null;
        this.timeStamp = LocalDateTime.now();
        this.volumeId = volumeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getValMax() {
        return valMax;
    }

    public void setValMax(Integer valMax) {
        this.valMax = valMax;
    }

    public Integer getValMin() {
        return valMin;
    }

    public void setValMin(Integer valMin) {
        this.valMin = valMin;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }

    public static SensorDTO from(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getValor(),
                sensor.getTipo().getId(),
                sensor.getEstado(),
                sensor.getValMax(),
                sensor.getValMin(),
                sensor.getVolume().getId()
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::from).collect(Collectors.toList());
    }
}
