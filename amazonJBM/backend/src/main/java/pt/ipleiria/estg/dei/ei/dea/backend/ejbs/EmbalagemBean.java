package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EmbalagemBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    SensorBean sensorBean;

    public void create(int id,int id_produto, int id_tipoEmbalagem,int quantidade_produtos, int id_volume, List<Integer> id_sensores) {

        Produto produto = em.find(Produto.class, id_produto);
        Tipo_Embalagem tipoEmbalagem = em.find(Tipo_Embalagem.class, id_tipoEmbalagem);
        Volume volume = em.find(Volume.class, id_volume);

        Embalagem embalagem = new Embalagem(id,produto,volume,quantidade_produtos, tipoEmbalagem);
        for(Integer id_sensor : id_sensores){
            Sensor sensor = em.find(Sensor.class,id_sensor);

            if(sensor != null){
                embalagem.addSensor(sensor);
            }
        }

        em.persist(embalagem);
    }

    public Embalagem find(int id) {
        return em.find(Embalagem.class, id);
    }

    public Response removerSensor(int id_embalagem, int id_sensor) {

        Embalagem embalagem = this.find(id_embalagem);

        if(embalagem == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Embalagem não encontrada!").build();
        }

        if(!embalagem.getVolume().getEncomenda().getEstado().equals("EmProcessamento")){
            return Response.status(Response.Status.BAD_REQUEST).entity("Apenas pode desassociar um sensor de encomendas 'Em Processamento'!").build();
        }

        Sensor sensor = sensorBean.find(id_sensor);

        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor não encontrada!").build();
        }

        if(!embalagem.sensorExiste(sensor)){
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor não pertence à embalagem especificada!").build();
        }

        embalagem.removerSensor(sensor);

        em.createNamedQuery("eliminarAlertas")
                .setParameter("sensorId", sensor.getId())
                .executeUpdate();

        em.createNamedQuery("eliminarLeituras")
                .setParameter("sensorId", sensor.getId())
                .executeUpdate();

        em.createNamedQuery("eliminarSensor")
                .setParameter("sensorId", sensor.getId())
                .executeUpdate();

        return Response.status(Response.Status.OK).entity("Sensor removido com sucesso!").build();
    }
}
