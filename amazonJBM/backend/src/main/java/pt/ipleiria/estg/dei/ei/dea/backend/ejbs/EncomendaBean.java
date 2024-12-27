package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
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

    public Encomenda create(String client_username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega) {

        Cliente cliente = clienteBean.find(client_username);
        //TODO -> se o cliente nao existir
        Encomenda encomenda = new Encomenda(cliente,estado,data_expedicao,data_entrega);
        em.persist(encomenda);
        em.flush();

        return encomenda;
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

        var encomenda = this.find(id);

        if (encomenda == null) {
            throw new EntityNotFoundException("Encomenda com ID " + id + " não encontrada.");
        }

        encomenda.setEstado(estado);

        if(estado.equalsIgnoreCase("Entregue")){

            List<Volume> volumes = encomenda.getVolumes();

            for (Volume volume : volumes) {
                /*for(Sensor sensor : volume.getSensores()){
                    sensor.setEstado("inativo");
                }*/
            }
        }

        try {
            em.merge(encomenda);
        } catch (Exception e) {
            throw new PersistenceException("Erro ao atualizar encomenda.", e);
        }
    }

    public void gerarVolumes(int id_encomenda,List<ProdutoDTO> produtos){

        for (ProdutoDTO produto:produtos) {
            //volumeBean.create(produto.getId(), produto.getQuantidade_por_volume(), id_encomenda);
        }
    }

    public List<Object[]> getCoordenadasEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id);

        List<Object[]> coordenadasList = new ArrayList<>();

        for (Volume volume : encomenda.getVolumes()) {
            /*for (Sensor sensor : volume.getSensores()) {
                if ("GPS".equals(sensor.getTipo().getTipo())) {
                    coordenadasList.add(new Object[]{volume.getId(), volume.getProduto().getNome(), sensor.getValor()});
                }
            }*/
        }

        return coordenadasList;
    }
}
