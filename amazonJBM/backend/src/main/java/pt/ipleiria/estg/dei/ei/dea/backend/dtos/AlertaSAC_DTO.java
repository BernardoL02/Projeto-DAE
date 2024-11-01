package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlertaSAC_DTO implements Serializable {

    private int id;

    private String mensagem;

    private int id_sensor;

    private String valor;

    private int id_encomenda;

    private LocalDateTime time_stamp;

    public AlertaSAC_DTO(int id, String mensagem, int id_sensor, String valor, int id_encomenda, LocalDateTime time_stamp){
        this.id = id;
        this.mensagem = mensagem;
        this.id_sensor = id_sensor;
        this.valor = valor;
        this.id_encomenda = id_encomenda;
        this.time_stamp = time_stamp;
    }

    public AlertaSAC_DTO(){

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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(int id_sensor) {
        this.id_sensor = id_sensor;
    }

    public int getId_encomenda() {
        return id_encomenda;
    }

    public void setId_encomenda(int id_encomenda) {
        this.id_encomenda = id_encomenda;
    }

    public LocalDateTime getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(LocalDateTime time_stamp) {
        this.time_stamp = time_stamp;
    }

    public static AlertaSAC_DTO from(Alerta alerta) {
        return new AlertaSAC_DTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getSensor().getId(),
                alerta.getValor(),
                alerta.getVolume().getEncomenda().getId(),
                alerta.getTime_stamp()
        );
    }

    public static List<AlertaSAC_DTO> from(List<Alerta> alerta) {
        return alerta.stream().map(AlertaSAC_DTO::from).collect(Collectors.toList());
    }
}
