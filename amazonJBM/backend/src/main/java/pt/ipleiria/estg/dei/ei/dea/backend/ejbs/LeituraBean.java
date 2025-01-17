package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Leitura;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;

import java.time.LocalDateTime;

@Stateless
public class LeituraBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private AlertaBean alertaBean;

    public Response create(int id_sensor, int bateria, String valor){
        Sensor sensor = sensorBean.find(id_sensor);

        if(!sensor.getEmbalagem().getVolume().getEncomenda().getEstado().equals("PorEntregar")){
            return Response.status(Response.Status.BAD_REQUEST).entity("Apenas é possivel criar leituras de sensores para encomendas no estado 'Por Entregar'").build();
        }

        Leitura leitura = new Leitura(sensor, bateria, valor);

        sensor.setValor(valor);
        sensor.setBateria(bateria);
        sensor.setTimeStamp(leitura.getTimeStamp());

        if (!sensor.getTipo().getTipo().equals("GPS") &&
                (Double.parseDouble(sensor.getValor()) > sensor.getValMax() ||
                        Double.parseDouble(sensor.getValor()) < sensor.getValMin())) {

            String maxMin = Integer.parseInt(sensor.getValor())  > sensor.getValMax()  ? "máximo" : "mínimo";
            int valorMaxMin = Integer.parseInt(sensor.getValor())  > sensor.getValMax()  ? sensor.getValMax() : sensor.getValMin();
            String mensagem = sensor.getEmbalagem().getProduto().getNome() + " - " +
                    sensor.getTipo().getTipo() + " excedeu o limite " + maxMin + " de " + valorMaxMin + "!";

            alertaBean.create(mensagem, sensor.getId(),sensor.getValor(),sensor.getBateria(),sensor.getEmbalagem().getVolume().getId());
        }

        em.persist(leitura);

        return Response.ok("Leitura criada com sucesso").build();
    }
}
