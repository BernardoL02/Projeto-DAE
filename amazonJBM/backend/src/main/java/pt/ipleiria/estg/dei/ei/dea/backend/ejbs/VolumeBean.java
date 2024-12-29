package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ResVolumeDetalhesDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.ArrayList;
import java.util.List;

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
        List<ProdutoCreateEncomendaDTO> produtosDTO = volumeCreateEncomendaDTO.getProdutos();

        for (ProdutoCreateEncomendaDTO produto : produtosDTO) {
            Produto produto1 = em.find(Produto.class, produto.getId());

            if(produto1 == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Produto não encontrado!").build();
            }

            produtos.add(produto1);
        }

        Volume volume = new Volume(encomenda);
        em.persist(volume);

        Integer indice_produto = 0;
        for(Produto produto : produtos){
            Embalagem embalagem = new Embalagem(produto, volume,  produtosDTO.get(indice_produto++).getQuantidade_de_produtos_comprados());
            em.persist(embalagem);

            volume.addEmbalagem(embalagem);
        }

        return Response.status(Response.Status.OK).entity("Volume associado à encomenda com sucesso!").build();
    }

    public Response verDetalhesVolume(int id_volume, Utilizador user){

        Volume volume = this.find(id_volume);

        if(volume == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Volume não encontrado!").build();
        }

        if(user.isCliente() && !volume.getEncomenda().getCliente().getUsername().equals(user.getUsername())){
            return Response.status(Response.Status.NOT_FOUND).entity("Apenas pode ver os detalhes de volumes que lhe pertencem.").build();
        }

        return Response.ok(ResVolumeDetalhesDTO.from(volume)).build();
    }
}
