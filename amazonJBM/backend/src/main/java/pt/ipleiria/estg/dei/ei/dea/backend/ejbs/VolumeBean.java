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

    public void create(int id, int id_produto, int quantidade, int id_encomenda){

        Produto produto = em.find(Produto.class, id_produto);
        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);
        var volume = new Volume(id, produto,quantidade, encomenda);
        encomenda.addVolume(volume);
        volume.setEncomenda(encomenda);
        em.persist(volume); //TODO verificar quantida por volume aquilo das caixas !! quantida_por_volume
    }

    public Volume find(int id) {
        var volume = em.find(Volume.class, id);
        if (volume == null) {
            throw new NoSuchElementException("Volume com ID " + id + " não encontrado.");
        }
        return volume;
    }

}
