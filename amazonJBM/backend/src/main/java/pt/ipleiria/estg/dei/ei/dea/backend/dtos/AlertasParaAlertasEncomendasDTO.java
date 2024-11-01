package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;

public class AlertasParaAlertasEncomendasDTO {
    private int id;
    private String mensagem;
    private String valor;
    private String timeStamp;

    public AlertasParaAlertasEncomendasDTO(int id, String mensagem, String valor, String timeStamp) {
        this.id = id;
        this.mensagem = mensagem;
        this.valor = valor;
        this.timeStamp = timeStamp;
    }
    public AlertasParaAlertasEncomendasDTO(){}

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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static AlertasParaAlertasEncomendasDTO from(Alerta alerta) {
        return new AlertasParaAlertasEncomendasDTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getValor(),
                alerta.getTime_stamp().toString()
        );
    }
}
