package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlertaDTO implements Serializable {

    private int id;

    private String mensagem;

    private int id_sensor;

    private String valor;

    private int id_encomenda;

    private int id_volume;

    private int bateria;

    private LocalDateTime time_stamp;

    public AlertaDTO(int id, String mensagem, int id_sensor, String valor,int bateria, int id_encomenda, int id_volume,LocalDateTime time_stamp) {
        this.id = id;
        this.mensagem = mensagem;
        this.id_sensor = id_sensor;
        this.valor = valor;
        this.bateria = bateria;
        this.id_encomenda = id_encomenda;
        this.id_volume= id_volume;
        this.time_stamp = time_stamp;
    }

    public AlertaDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(int id_sensor) {
        this.id_sensor = id_sensor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId_encomenda() {
        return id_encomenda;
    }

    public void setId_encomenda(int id_encomenda) {
        this.id_encomenda = id_encomenda;
    }

    public int getId_volume() {
        return id_volume;
    }

    public void setId_volume(int id_volume) {
        this.id_volume = id_volume;
    }

    public LocalDateTime getTime_stamp() {
        return time_stamp;
    }

    public int getBateria() {
        return bateria;
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public void setTime_stamp(LocalDateTime time_stamp) {
        this.time_stamp = time_stamp;
    }

    public static AlertaDTO from(Alerta alerta) {
        return new AlertaDTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getSensor().getId(),
                alerta.getValor(),
                alerta.getSensor().getBateria(),
                alerta.getVolume().getEncomenda().getId(),
                alerta.getVolume().getId(),
                alerta.getTime_stamp()
        );
    }

    public static List<AlertaDTO> from(List<Alerta> alerta) {
        return alerta.stream().map(AlertaDTO::from).collect(Collectors.toList());
    }
}
