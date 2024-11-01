package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ResAlertasSensorDTO implements Serializable {

    private int id;
    private String menssagem;
    private String valor;
    private LocalDateTime data;
    private int id_encomenda;

    public ResAlertasSensorDTO(int id, String menssagem, String valor, LocalDateTime data, int id_encomenda) {
        this.id = id;
        this.menssagem = menssagem;
        this.valor = valor;
        this.data = data;
        this.id_encomenda = id_encomenda;
    }

    public ResAlertasSensorDTO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getId_encomenda() {
        return id_encomenda;
    }

    public void setId_encomenda(int id_encomenda) {
        this.id_encomenda = id_encomenda;
    }

    public static ResAlertasSensorDTO from(Alerta alerta) {
        return new ResAlertasSensorDTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getValor(),
                alerta.getTime_stamp(),
                alerta.getVolume().getEncomenda().getId()
        );
    }

    public static List<ResAlertasSensorDTO> from(List<Alerta> alerta) {
        return alerta.stream().map(ResAlertasSensorDTO::from).collect(Collectors.toList());
    }
}
