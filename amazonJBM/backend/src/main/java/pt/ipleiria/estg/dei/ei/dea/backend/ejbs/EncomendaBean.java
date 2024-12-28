package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.UtilizadorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EncomendaBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private VolumeBean volumeBean;

    public Response create(String client_username, List<VolumeDTO> volumes, String estado, LocalDateTime data_expedicao) {
        Cliente cliente = clienteBean.find(client_username);

        if(cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente não encontrado!").build();

        }

        Encomenda encomenda = new Encomenda(cliente,estado,data_expedicao);
        em.persist(encomenda);

            for(VolumeDTO volume: volumes) {
                Volume volume1 = new Volume(encomenda);
                em.persist(volume1);
                for (ProdutoDTO produto : volume.getProdutos()) {
                    Produto produto1 = em.find(Produto.class, produto.getId());
                    Embalagem embalagem = new Embalagem(produto1, volume1, produto.getQuantidade_de_produtos_comprados());
                    volume1.addEmbalagem(embalagem);
                    em.persist(embalagem);
                }
                encomenda.addVolume(volume1);
        }

        return Response.ok("Encomenda criada com sucesso com ID: " + encomenda.getId()).build();
    }

    public List<Encomenda> findAll(Utilizador user) {

        List<Encomenda> encomendas = new ArrayList<>();

        if (user.getRole().equals("Cliente")) {
            Cliente cliente = em.find(Cliente.class, user.getUsername());
            Hibernate.initialize(cliente.getEncomendas());
            encomendas = cliente.getEncomendas();
        }
        else{
            encomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        }

        return encomendas;
    }

    public Encomenda find(int id) {
        var encomenda = em.find(Encomenda.class, id);
        if (encomenda == null) {
            throw new NoSuchElementException("Encomenda com ID " + id + " não encontrada.");
        }
        Hibernate.initialize(encomenda);
        return encomenda;
    }

    public Encomenda findEncomendaById(int id) {

        var encomenda = em.find(Encomenda.class, id);

        return encomenda;
    }

    public List<Encomenda> findEncomendasByEstado(String estado, Utilizador user) {
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();

        if(user.isCliente()){
            for (Encomenda encomenda : todasEncomendas) {
                if (encomenda.getEstado().equals(estado) && encomenda.getCliente().getUsername().equals(user.getUsername())){
                    encomendasFiltradas.add(encomenda);
                }
            }
        }
        else{
            for (Encomenda encomenda : todasEncomendas) {
                if (encomenda.getEstado().equals(estado)){
                    encomendasFiltradas.add(encomenda);
                }
            }
        }


        return encomendasFiltradas;
    }

    public List<Encomenda> findPendentes(){
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        for (Encomenda encomenda : todasEncomendas) {
            if (encomenda.getEstado().equals("EmProcessamento") || encomenda.getEstado().equals("PorEntregar")) {
                Hibernate.initialize(encomenda.getVolumes());
                encomendasFiltradas.add(encomenda);
            }
        }
        return encomendasFiltradas;
    }


    public void mudarEstadoEncomenda(int id, String estado) {
        //Todo
        /*
        var encomenda = this.find(id);

        if (encomenda == null) {
            throw new EntityNotFoundException("Encomenda com ID " + id + " não encontrada.");
        }

        encomenda.setEstado(estado);

        if(estado.equalsIgnoreCase("Entregue")){

            List<Volume> volumes = encomenda.getVolumes();

            for (Volume volume : volumes) {
                for(Sensor sensor : volume.getSensores()){
                    sensor.setEstado("inativo");
                }
            }
        }

        try {
            em.merge(encomenda);
        } catch (Exception e) {
            throw new PersistenceException("Erro ao atualizar encomenda.", e);
        }
        */
    }

    public void gerarVolumes(int id_encomenda,List<ProdutoDTO> produtos){
        //TODO
        /*
        for (ProdutoDTO produto:produtos) {
            volumeBean.create(produto.getId(), produto.getQuantidade_por_volume(), id_encomenda);
        }
         */
    }


    public List<Object[]> getCoordenadasEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id);

        List<Object[]> coordenadasList = new ArrayList<>();

        for (Volume volume : encomenda.getVolumes()) {
            for(Embalagem embalagem : volume.getEmbalagens())
            for (Sensor sensor : embalagem.getSensores()) {
                if ("GPS".equals(sensor.getTipo().getTipo())) {
                    coordenadasList.add(new Object[]{volume.getId(), embalagem.getProduto().getNome(), sensor.getValor()});
                }
            }
        }

        return coordenadasList;
    }
}
