package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EmbalagemBean {

    @PersistenceContext
    private EntityManager em;

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

}
