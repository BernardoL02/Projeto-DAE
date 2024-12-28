package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Embalagem;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Volume;

import java.util.ArrayList;
import java.util.List;
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
        return em.find(Volume.class, id);
    }

    public Response associarVolumeEncomenda(int id_encomenda, VolumeCreateEncomendaDTO volumeCreateEncomendaDTO){

        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);

        if(encomenda == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Encomenda não encontrada!").build();
        }

        List<Produto> produtos = new ArrayList<>();

        for (ProdutoDTO produto : volumeCreateEncomendaDTO.getProdutos()) {
            Produto produto1 = em.find(Produto.class, produto.getId());

            if(produto1 == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Produto não encontrado!").build();
            }
            produtos.add(produto1);
        }

        Volume volume = new Volume(encomenda);
        em.persist(volume);

        for(Produto produto : produtos){
            Embalagem embalagem = new Embalagem(produto, volume, produto.getQuantidade_de_produtos_comprados());
            em.persist(embalagem);

            volume.addEmbalagem(embalagem);
        }
        return Response.status(Response.Status.OK).entity("Volume associado à encomenda com sucesso!").build();
    }

}
