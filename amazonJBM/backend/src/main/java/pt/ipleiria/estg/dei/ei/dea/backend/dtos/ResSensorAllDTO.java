package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import jakarta.persistence.Column;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ResSensorAllDTO implements Serializable {

    private int id;
    private String valor;
    private String tipoNome;
    private String estado;
    private int bateria;
    private LocalDateTime timeStamp;
    private int idEmbalagem;
    private int idVolume;
    private int idEncomenda;
    private Integer valMax;
    private Integer valMin;

    public ResSensorAllDTO(int id, String valor, String tipoNome, String estado, int bateria, LocalDateTime timeStamp, int idEmbalagem ,int idVolume, int idEncomenda, Integer valMax, Integer valMin) {
        this.id = id;
        this.valor = valor;
        this.tipoNome = tipoNome;
        this.estado = estado;
        this.bateria = bateria;
        this.timeStamp = timeStamp;
        this.idEmbalagem = idEmbalagem;
        this.idVolume = idVolume;
        this.idEncomenda = idEncomenda;
        this.valMax = valMax;
        this.valMin = valMin;
    }

    public ResSensorAllDTO(){

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

    public int getIdEmbalagem() {
        return idEmbalagem;
    }

    public void setIdEmbalagem(int idEmbalagem) {
        this.idEmbalagem = idEmbalagem;
    }

    public int getIdVolume() {
        return idVolume;
    }

    public void setIdVolume(int idVolume) {
        this.idVolume = idVolume;
    }

    public int getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(int idEncomenda) {
        this.idEncomenda = idEncomenda;
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

    public static ResSensorAllDTO from(Sensor sensor) {
        return new ResSensorAllDTO(
                sensor.getId(),
                sensor.getValor(),
                sensor.getTipo().getTipo(),
                sensor.getEstado(),
                sensor.getBateria(),
                sensor.getTimeStamp(),
                sensor.getEmbalagem().getId(),
                sensor.getEmbalagem().getVolume().getId(),
                sensor.getEmbalagem().getVolume().getEncomenda().getId(),
                sensor.getValMax(),
                sensor.getValMin()
        );
    }

    public static List<ResSensorAllDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(ResSensorAllDTO::from).collect(Collectors.toList());
    }

}
