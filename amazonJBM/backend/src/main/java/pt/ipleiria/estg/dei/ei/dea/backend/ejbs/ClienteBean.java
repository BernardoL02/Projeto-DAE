package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.util.*;

@Stateless
public class ClienteBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome, String morada ){
        var cliente = new Cliente(username, password, email, nome, morada);
        em.persist(cliente);
    }

    public List<Cliente> findAll() {
        List<Cliente> clientes = em.createNamedQuery("getAllClientes", Cliente.class).getResultList();
        return clientes;
    }

    public Cliente find(String username){

        var cliente = em.find(Cliente.class, username);

        if(cliente == null){
            throw new NoSuchElementException();
        }

        return cliente;
    }


    public Cliente findWithEncomendas(String username){

        var cliente = this.find(username);
        Hibernate.initialize(cliente.getEncomendas());

        for (Encomenda encomenda : cliente.getEncomendas()) {
            Hibernate.initialize(encomenda.getVolumes());
        }

        return cliente;
    }

    public Encomenda findEncomendaById(String username, int encomendaId) {

        var cliente = this.find(username);

        Hibernate.initialize(cliente.getEncomendas());

        for (Encomenda encomenda : cliente.getEncomendas()) {
            if (encomenda.getId() == encomendaId) {
                Hibernate.initialize(encomenda.getVolumes());
                return encomenda;
            }
        }

        return null; //TODO -> Retornar codigo apropriado
    }

    public List<Encomenda> findEncomendaByEstado(String username, String estado) {

        var cliente = this.find(username);

        Hibernate.initialize(cliente.getEncomendas());

        List<Encomenda> encomendasFiltradas = new ArrayList<>();

        for (Encomenda encomenda : cliente.getEncomendas()) {
            if (encomenda.getEstado().equals(estado)) {
                Hibernate.initialize(encomenda.getVolumes());
                encomendasFiltradas.add(encomenda);
            }
        }

        return encomendasFiltradas;
    }

    public void cancelarEncomenda(int id, String username) {

        var cliente = this.find(username);

        Encomenda encomenda = cliente.getEncomenda(id); //Todo -> Ver se encomenda existe

        encomenda.setEstado("Cancelada");
        em.merge(encomenda);
    }

    public List<Sensor> getUltimaLeituraSensoresByTipo(String tipo_sensor, String username) {
        var cliente = this.find(username);
        Hibernate.initialize(cliente.getEncomendas());

        List<Sensor> sensores = new ArrayList<>();

        for (Encomenda encomenda : cliente.getEncomendas()) {
            for (Volume volume : encomenda.getVolumes()) {
                for (Sensor sensor : volume.getSensores()) {
                    if (sensor.getTipo().getTipo().equals(tipo_sensor)) {
                        sensores.add(sensor);
                    }
                }
            }
        }

        return sensores;
    }

    public List<Alerta> getAlertasByEncomenda(int id, String username) {
        return em.createNamedQuery("getAlertasByEncomendaId", Alerta.class).setParameter("encomendaId", id).getResultList();
    }
}
