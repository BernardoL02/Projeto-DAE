package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class VolumeBean {

    @PersistenceContext
    private EntityManager em;

    public void create(List<Integer> ids_produtos, int id_encomenda){

        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);

        if(encomenda== null){
            return; //TODO mensagem de erro apropriada
        }

        var volume = new Volume( encomenda);

        for (Integer produto_id : ids_produtos) {
            Produto produto = em.find(Produto.class, produto_id);

            if(produto != null){
                volume.addProduto(produto);
            }
        }

        encomenda.addVolume(volume);
        volume.setEncomenda(encomenda);
        em.persist(volume);
    }

    public Volume find(int id) {
        var volume = em.find(Volume.class, id);
        if (volume == null) {
            throw new NoSuchElementException("Volume com ID " + id + " não encontrado.");
        }
        return volume;
    }

}
