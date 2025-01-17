package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;

public class AlertasParaAlertasEncomendasDTO {
    private int id;
    private String mensagem;
    private String valor;
    private String timeStamp;
    private int id_encomenda;
    private int id_volume;
    private int id_embalagem;

    public AlertasParaAlertasEncomendasDTO(int id, String mensagem, String valor, String timeStamp,int id_volume, int id_embalagem,  int id_encomenda) {
        this.id = id;
        this.mensagem = mensagem;
        this.valor = valor;
        this.timeStamp = timeStamp;
        this.id_volume = id_volume;
        this.id_embalagem = id_embalagem;
        this.id_encomenda = id_encomenda;
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

    public int getId_embalagem() {
        return id_embalagem;
    }

    public void setId_embalagem(int id_embalagem) {
        this.id_embalagem = id_embalagem;
    }

    public static AlertasParaAlertasEncomendasDTO from(Alerta alerta) {
        return new AlertasParaAlertasEncomendasDTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getValor(),
                alerta.getTime_stamp().toString(),
                alerta.getVolume().getId(),
                alerta.getSensor().getEmbalagem().getId(),
                alerta.getVolume().getEncomenda().getId()
        );
    }
}
