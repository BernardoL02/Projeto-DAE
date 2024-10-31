package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EncomendasDTO;
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

    public void create(int id, String client_username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega) {

        Cliente cliente = clienteBean.find(client_username);

        Encomenda encomenda = new Encomenda(id,cliente,estado,data_expedicao,data_entrega);
        em.persist(encomenda);
    }

    public void create(int id, String client_username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega,List<Produto> produtos) {

        Cliente cliente = clienteBean.find(client_username);
        Encomenda encomenda = new Encomenda(id,cliente,estado,data_expedicao,data_entrega);
        em.persist(encomenda);
    }


    public Encomenda find(int id) {
        var encomenda = em.find(Encomenda.class, id);
        if (encomenda == null) {
            throw new NoSuchElementException("Encomenda com ID " + id + " não encontrado.");
        }
        Hibernate.initialize(encomenda);
        return encomenda;
    }

    public List<Encomenda> findEncomendasByEstado(String estado) {
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        for (Encomenda encomenda : todasEncomendas) {
            if (encomenda.getEstado().equals(estado)){
                encomendasFiltradas.add(encomenda);
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


    public void entregarEncomenda(int id) {

        var encomenda = this.find(id);
        System.out.println(encomenda);
        if (encomenda == null) {
            throw new EntityNotFoundException("Encomenda com ID " + id + " não encontrada.");
        }
        encomenda.setEstado("Entregue");
        try {
            em.merge(encomenda);
        } catch (Exception e) {
            throw new PersistenceException("Erro ao atualizar encomenda.", e);
        }
    }

    public void generarVolumes(int id_encomenda,List<Produto> produtos){
        int id_lasVolume = 5;
        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);

        for (Produto produto:produtos) {
            volumeBean.create(id_lasVolume, produto.getId(),produto.getQuantidade_por_volume(), id_encomenda);
            encomenda.addVolume(em.find(Volume.class, id_lasVolume));
            id_lasVolume++;
        }
    }
}
