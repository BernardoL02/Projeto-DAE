package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResAlertasEncomendaDTO implements Serializable {
    private List<SensorAlertasDTO> sensores;

    public ResAlertasEncomendaDTO(List<SensorAlertasDTO> sensores) {
        this.sensores = sensores;
    }

    public List<SensorAlertasDTO> getSensores() {
        return sensores;
    }

    public static ResAlertasEncomendaDTO from(List<Alerta> alertas) {
        Map<Integer, List<Alerta>> alertasPorSensor = alertas.stream()
                .collect(Collectors.groupingBy(a -> a.getSensor().getId()));

        List<SensorAlertasDTO> sensores = alertasPorSensor.entrySet().stream()
                .map(entry -> {
                    int sensorId = entry.getKey();
                    Alerta alertaExemplo = entry.getValue().get(0);
                    String tipoSensor = alertaExemplo.getSensor().getTipo().getTipo();

                    List<AlertasParaAlertasEncomendasDTO> alertaDTOs = entry.getValue().stream()
                            .map(AlertasParaAlertasEncomendasDTO::from)
                            .collect(Collectors.toList());

                    return new SensorAlertasDTO(sensorId, tipoSensor, alertaDTOs);
                })
                .collect(Collectors.toList());

        return new ResAlertasEncomendaDTO(sensores);
    }
}
