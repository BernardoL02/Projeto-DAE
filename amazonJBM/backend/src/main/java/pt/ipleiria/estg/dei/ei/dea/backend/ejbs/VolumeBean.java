package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.util.NoSuchElementException;

@Stateless
public class VolumeBean {

    @PersistenceContext
    private EntityManager em;

    public Volume create(int id_encomenda){

        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);
        Volume volume = new Volume(encomenda);

        encomenda.addVolume(volume);
        volume.setEncomenda(encomenda);

        em.persist(volume);

        return volume;
    }

    public Volume find(int id) {
        System.out.println("Procurando volume com ID: " + id);
        var volume = em.find(Volume.class, id);
        System.out.println("Resultado da busca: " + volume);
        if (volume == null) {
            throw new NoSuchElementException("Volume com ID " + id + " não encontrado.");
        }
        return volume;
    }

}
