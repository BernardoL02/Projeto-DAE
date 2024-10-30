package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO implements Serializable {
    private int id;
    private String valor;
    private int tipoId;
    private String tipoNome;
    private String estado;
    private int bateria;
    private Integer valMax;
    private Integer valMin;
    private LocalDateTime timeStamp;
    private int volumeId;

    public SensorDTO() {}

    public SensorDTO(int id, String valor, int tipoId, String tipoNome, String estado, int bateria, Integer valMax, Integer valMin, int volumeId) {
        this.id = id;
        this.valor = valor;
        this.tipoId = tipoId;
        this.tipoNome = tipoNome;
        this.estado = estado;
        this.bateria = bateria;
        this.valMax = valMax;
        this.valMin = valMin;
        this.timeStamp = LocalDateTime.now();
        this.volumeId = volumeId;
    }

    public SensorDTO(int id, String valor, int tipoId, String tipoNome, String estado, int bateria, int volumeId) {
        this.id = id;
        this.valor = valor;
        this.tipoId = tipoId;
        this.tipoNome = tipoNome;
        this.estado = estado;
        this.bateria = bateria;
        this.valMax = null;
        this.valMin = null;
        this.timeStamp = LocalDateTime.now();
        this.volumeId = volumeId;
    }


    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
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

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
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
                sensor.getTipo().getTipo(),
                sensor.getEstado(),
                sensor.getBateria(),
                sensor.getValMax(),
                sensor.getValMin(),
                sensor.getVolume().getId()
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::from).collect(Collectors.toList());
    }
}
