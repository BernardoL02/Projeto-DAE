package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class VolumeBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private EmbalagemBean embalagemBean;

    @EJB
    private TipoEmbalagemBean tipoEmbalagemBean;

    public Volume create(int id,int id_encomenda){

        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);
        Volume volume = new Volume(id,encomenda);

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

        List<EmbalagemCreateEncomendaDTO> embalagensDTO = volumeCreateEncomendaDTO.getEmbalagens();
        List<Produto> produtos = new ArrayList<>();
        List<Tipo_Embalagem> tiposEmbalagem = new ArrayList<>();

        for (EmbalagemCreateEncomendaDTO embalagem : embalagensDTO) {

            Tipo_Embalagem tipoEmbalagem = tipoEmbalagemBean.find(embalagem.getTipo());

            if(tipoEmbalagem == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Tipo de embalagem não encontrada!").build();
            }

            Produto produto1 = em.find(Produto.class, embalagem.getProduto().getId());
            if(produto1 == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Produto não encontrado!").build();
            }

            tiposEmbalagem.add(tipoEmbalagem);
            produtos.add(produto1);
        }

        Volume volume = new Volume(volumeCreateEncomendaDTO.getId(),encomenda);
        em.persist(volume);

        for(int i = 0 ; i < tiposEmbalagem.size(); i++){
            Embalagem embalagem = new Embalagem(volumeCreateEncomendaDTO.getEmbalagens().get(i).getId(), produtos.get(i), volume, embalagensDTO.get(i).getQuantidade(), tiposEmbalagem.get(i));
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

    public Response entregarVolume(int id_volume){
        Volume volume = this.find(id_volume);
        if(volume == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Volume não encontrado!").build();
        }

        if(volume.getEntregue()){
            return Response.status(Response.Status.NOT_FOUND).entity("Este volume já foi entregue!").build();
        }

        if(!volume.getEncomenda().getEstado().equals("PorEntregar")){
            return Response.status(Response.Status.BAD_REQUEST).entity("Impossivel entregar volume, encomenda não está no estado 'Por Entregar'. ").build();
        }

        volume.setEntregue(true);
        return Response.ok(ResVolumeDetalhesDTO.from(volume)).build();
    }
}
