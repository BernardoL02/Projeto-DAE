package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.List;
import java.util.NoSuchElementException;
@Stateless
public class TipoEmbalagemBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String name, List<Integer> ids_sensores) {
        Tipo_Embalagem embalagem = new Tipo_Embalagem(name);

        for (Integer sensor_id : ids_sensores) {
            Tipo_Sensores tipoSensores = em.find(Tipo_Sensores.class, sensor_id);

            if(tipoSensores != null){
                embalagem.addTipoSensor(tipoSensores);
            }
        }

        em.persist(embalagem);
    }

    public Tipo_Embalagem find(int id) {
        var embalagem = em.find(Tipo_Embalagem.class, id);
        if (embalagem == null) {
            throw new NoSuchElementException("Embalagem com ID " + id + " não encontrado.");
        }
        return embalagem;
    }


}
