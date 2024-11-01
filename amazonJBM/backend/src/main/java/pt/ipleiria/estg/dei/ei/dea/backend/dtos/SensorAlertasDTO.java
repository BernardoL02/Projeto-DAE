package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import java.io.Serializable;
import java.util.List;

public class SensorAlertasDTO implements Serializable {
    private int id;
    private String tipo;
    private List<AlertasParaAlertasEncomendasDTO> alertas;

    public SensorAlertasDTO(int id, String tipo, List<AlertasParaAlertasEncomendasDTO> alertas) {
        this.id = id;
        this.tipo = tipo;
        this.alertas = alertas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setAlertas(List<AlertasParaAlertasEncomendasDTO> alertas) {
        this.alertas = alertas;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public List<AlertasParaAlertasEncomendasDTO> getAlertas() {
        return alertas;
    }
}
